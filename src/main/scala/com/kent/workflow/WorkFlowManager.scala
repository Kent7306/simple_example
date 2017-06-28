package com.kent.workflow

import akka.actor.Actor
import akka.actor.ActorLogging
import com.kent.coordinate.Coordinator
import akka.actor.ActorRef
import akka.actor.Terminated
import akka.actor.Props
import com.kent.workflow.WorkFlowManager._
import com.kent.workflow.WorkFlow.WStatus._
import akka.actor.PoisonPill

class WorkFlowManager extends Actor with ActorLogging{
  var workflows: Map[String, WorkFlow] = Map()
  var workflowActors: Map[String,Tuple2[String,ActorRef]] = Map()
  var coordinatorManager: ActorRef = _
  /**
   * 增
   */
  def add(wf: WorkFlow): Boolean = {
    workflows = workflows + (wf.name -> wf)
    //println(s"工作流管理器中工作流： ${workflows}")
    true
  }
  /**
   * 删
   */
  def remove(name: String): Boolean = {
    workflows = workflows.filterNot {x => x._1 == name}.toMap
    true
  }
  /**
   * 改
   */
  def update(wf: WorkFlow): Boolean = {
    workflows = workflows.map {x => if(x._1 == wf.name) wf.name -> wf else x }.toMap
    true
  }
  /**
   * 初始化
   */
  def init(){
     ??? 
  }
  /**
   * 生成工作流实例并执行
   */
  def execute(wfName: String,params: Map[String, String]){
    import com.kent.workflow.WorkflowActor._
    val cloneWf = workflows(wfName).deepClone()
    cloneWf.params = params
    val wfActorRef = context.actorOf(Props(WorkflowActor(cloneWf, params)), cloneWf.actorName)
    workflowActors = workflowActors + (cloneWf.actorName -> (cloneWf.name,wfActorRef))
    var str = "";;;;;
    workflowActors.foreach( x => str = str + x._1+" ");;;;;
    println("<WorkFlowManager> workflow实例 actor个数：" + workflowActors.size + " 分别是：" + str);;;;
    wfActorRef ! Start()
  }
  /**
   * 工作流实例完成后
   */
  def handleWorkFlowInstanceFinished(wf: WorkFlow):Boolean = {
    val (wfname, af) = this.workflowActors.get(wf.actorName).get
    this.workflowActors = this.workflowActors.filterKeys { _ != wf.actorName }.toMap
    println("==============================")
    println(wf)
    println("==============================")
    coordinatorManager ! WorkFlowExecuteResult(wfname, wf.status)  
    true
  }
  /**
   * receive方法
   */
  def receive: Actor.Receive = {
    case AddWorkFlow(content) => this.add(WorkFlow(content))
    case RemoveWorkFlow(name) => this.remove(name)
    case UpdateWorkFlow(content) => this.update(WorkFlow(content))
    case NewAndExecuteWorkFlowInstance(name, params) => this.execute(name, params)
    case WorkFlowInstanceExecuteResult(wf) => this.handleWorkFlowInstanceFinished(wf)
    case GetCoordinatorManager(cm) => 
      coordinatorManager = cm
      context.watch(coordinatorManager)
    case Terminated(actorRef) => 
      if(coordinatorManager == actorRef) log.warning("coordinatorManager actor挂掉了...")
  }
}


object WorkFlowManager{
  def apply(wfs: List[WorkFlow]):WorkFlowManager = {
    val wfm = new WorkFlowManager;
    wfm.workflows = wfs.map { x => x.name -> x }.toMap
    wfm
  }
  def apply(contents: Set[String]):WorkFlowManager = {
    WorkFlowManager(contents.map { WorkFlow(_) }.toList)
  }

  
  case class NewAndExecuteWorkFlowInstance(wfName: String, params: Map[String, String])
  case class StopWorkFlowInstance(wfName: String)
  case class AddWorkFlow(content: String)
  case class RemoveWorkFlow(wfName: String)
  case class UpdateWorkFlow(content: String)
  
  case class WorkFlowInstanceExecuteResult(workflow: WorkFlow)
  case class WorkFlowExecuteResult(wfName: String, status: WStatus)
  case class GetCoordinatorManager(coorManager: ActorRef)
}