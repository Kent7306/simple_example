package com.kent.workflow

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.immutable.Queue
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Cancellable
import com.kent.workflow.ActionActor._
import com.kent.workflow.Node.Status._
import com.kent.workflow.WorkFlow.WStatus._
import akka.pattern.ask
import akka.actor.Props
import com.kent.util.Util
import scala.concurrent.Future
import akka.util.Timeout
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import com.kent.workflow.WorkFlowManager._

class WorkflowActor(private val _wf: WorkFlow,private val _param: Map[String, String]) extends Actor with ActorLogging {
  var workflowManageAcotrRef:ActorRef = _
  def wf = _wf
  //正在运行的节点actor
	var runningActors: Map[ActorRef, ActionNode] = Map()
	//节点等待执行队列
	var waitingNodes = Queue[Node]()
	var scheduler:Cancellable = _
  import com.kent.workflow.WorkflowActor._
  
  /**
   * 启动workflow
   */
  def start(){
	  log.info(s"[workflow:${this.wf.actorName}开始启动")
	  //节点替换参数
	   this.wf.nodeList.foreach { _.replaceParam(wf.params) }
	  //找到开始节点并加入到等待队列
    val sn = wf.getStartNode()
    if(!sn.isEmpty && sn.get.ifCanExecuted(wf)){
    	wf.startTime = Util.nowDate
    	waitingNodes = waitingNodes.enqueue(sn.get)
    }
    //启动队列
	  this.scheduler = context.system.scheduler.schedule(0 millis, 3 seconds){
      this.scan()
    }
  }
	/**
	 * 扫描
	 */
  def scan(){
    import com.kent.workflow.WorkFlowManager.WorkFlowInstanceExecuteResult
    if(waitingNodes.size > 0){
    	val(node, queue) = waitingNodes.dequeue
    	waitingNodes = queue
      println("----执行节点："+node.name+"， 类型："+node.getClass)
	    node.start(this) 
    }
  }
  /**
   * 处理action节点的返回状态????
   */
 private def handleActionReply(sta: Status, msg: String, actionSender: ActorRef){
    val node = runningActors(actionSender)
    runningActors = runningActors.filter(_._1 == actionSender).toMap
    node.status = sta
    node.actionExecutedMsg = msg
    node.terminate(this)
  }
	/**
	 * 终止
	 */
	def terminate(){
		scheduler.cancel()
	  runningActors = Map()
	  this.waitingNodes = Queue()
	  workflowManageAcotrRef ! WorkFlowInstanceExecuteResult(wf)
	  context.stop(self)
	}
	/**
	 * 创建并开始actor节点
	 */
	def createAndStartActionActor(actionNode: ActionNode):Boolean = {
	  val cloneNode = actionNode.deepClone().asInstanceOf[ActionNode]
	  val props = Props(new ActionActor(cloneNode)) 
		val actionActorRef = context.actorOf(props, cloneNode.name)
		this.runningActors += (actionActorRef -> actionNode)
		import com.kent.workflow.WorkflowActor.Start
		actionActorRef ! Start()
		true
	}
	
  def receive: Actor.Receive = {
    case Start() => workflowManageAcotrRef = sender;start()
    case Kill() => 
    case ActionExecuteResult(status, msg) => handleActionReply(status, msg, sender)
  }
  
  
}

object WorkflowActor {
  def apply(wf: WorkFlow, param: Map[String, String]): WorkflowActor = new WorkflowActor(wf, param)
  
  	/**
	 * 杀死所有运行的ActionActor
	 */
	def killRunningNodeActors(workflowActor: WorkflowActor){
	  implicit val timeout = Timeout(60 seconds)
	  //kill掉所有运行的actionactor
	  val futures = workflowActor.runningActors.map(x => {
	    val result = ask(x._1, Kill())
	                .mapTo[ActionExecuteResult]
	                .recover{ case e: Exception => ActionExecuteResult(FAILED,"节点超时")}
		  result.map { y => 
		      val node = workflowActor.runningActors(x._1)
		      node.status == y.status
		      node.actionExecutedMsg = y.msg
		      y 
		  }
	  }).toList
	  
	  val futuresSeq = Future.sequence(futures).onComplete { x => workflowActor.terminate()}
	}
  case class Start()
  case class Kill ()
}