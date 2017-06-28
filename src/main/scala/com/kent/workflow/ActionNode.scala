package com.kent.workflow

import akka.actor.Props
import com.kent.workflow.WorkFlow.WStatus._

abstract class ActionNode(private var _name:String) extends Node(_name) {
	import com.kent.workflow.Node.Status._
  var retryTimes:Int = 0
  var interval:Int = 0
  var timeout:Int = -1
  
  var ok: String = _
  var error: String = _ 
  
  /**
   * 深度克隆协助
   */
  def deepCloneAssist(an: ActionNode): ActionNode = {
    an.retryTimes = retryTimes
    an.interval = interval
    an.timeout = timeout
    an.ok = ok
    an.error = error
    an
  }
  /**
   * 得到下一个执行节点
   */
  def getNextNodes(wf: WorkFlow): List[Node] = {
    status match {
          case SUCCESSED => wf.nodeList.filter { _.name == this.ok }.toList
          case FAILED => wf.nodeList.filter { _.name == this.error }.toList
          case KILLED => wf.nodeList.filter { _.name == this.error }.toList
          case _ => throw new Exception(s"[workflow:${wf.name}]的[action:${this.name}]执行状态出错")
        }
  }

  def ifCanExecuted(wf: WorkFlow): Boolean = true

  def start(wfa: WorkflowActor): Boolean = wfa.createAndStartActionActor(this)
  /**
   * 找到下一执行节点
   */
  def terminate(wfa: WorkflowActor): Boolean = {
      this.status match {
      case SUCCESSED => 
        
      case FAILED => 
        if(this.getNextNodes(wfa.wf).size >0){  //若该action节点执行失败后有指定下一节点

        }else{
      	  wfa.wf.status = W_FAILED 
      		WorkflowActor.killRunningNodeActors(wfa)
      		return false
        }
      case KILLED =>
        wfa.wf.status = W_KILLED
        WorkflowActor.killRunningNodeActors(wfa)
        return false
    }
    
    val nodes = this.getNextNodes(wfa.wf)
    nodes.filter { _.ifCanExecuted(wfa.wf) }.foreach { x => wfa.waitingNodes = wfa.waitingNodes.enqueue(x)}
    return true
  }
  
  def terminate2(wfa: WorkflowActor): Boolean = {
    val nodes = this.getNextNodes(wfa.wf)
    nodes.filter { _.ifCanExecuted(wfa.wf) }.foreach { x => wfa.waitingNodes = wfa.waitingNodes.enqueue(x)}
    true
  }
}

object ActionNode {  
  def apply(node: scala.xml.Node): ActionNode = parseXmlNode(node);
  
  def parseXmlNode(node: scala.xml.Node):ActionNode = {
    val nameOpt = node.attribute("name")
		val retryOpt = node.attribute("retry-times")
	  val intervalOpt = node.attribute("interval")
		val timeoutOpt = node.attribute("timeout")
    if((node \ "@name").size != 1){    
  	  throw new Exception("存在action未配置name属性")
    }else if((node \ "ok").size != 1){
      throw new Exception("[action] "+nameOpt.get.text+":未配置[ok]标签")
    }else if((node \ "ok" \ "@to").size != 1){
       throw new Exception("[action] "+nameOpt.get.text+":-->[ok]:未配置name属性")
    }/*else if((node \ "error").size != 1){
      throw new Exception("[action] "+nameOpt.get.text+":未配置[error]标签")
    }else if((node \ "error" \ "@to").size != 1){
       throw new Exception("[action] "+nameOpt.get.text+":-->[error]:未配置name属性")
    }*/
    
    var actionNode: ActionNode = null
    //if((node \ "_").size != 1) throw new Exception(s"该[action:${nameOpt.get}]的子节点不唯一")

    val childNode = (node \ "_")(0)
    childNode match {
      case <host-script>{content @ _*}</host-script> => 
        actionNode = HostScriptActionNode(nameOpt.get.text, childNode)
      case <sub-workflow>{content @ _*}</sub-workflow> => 
        ???
      case _ => 
        throw new Exception(s"该[action:${nameOpt.get}]的类型不存在")
    }
    
    actionNode.retryTimes = if(!retryOpt.isEmpty){retryOpt.get.text.toInt}else{actionNode.retryTimes}
    actionNode.interval = if(!intervalOpt.isEmpty){intervalOpt.get.text.toInt}else{actionNode.interval}
    actionNode.timeout = if(!retryOpt.isEmpty){timeoutOpt.get.text.toInt}else{actionNode.timeout}
    actionNode.ok = (node \ "ok" \ "@to").text
    if((node \ "error").size != 1 && (node \ "error" \ "@to").size != 1){
    	actionNode.error = (node \ "error" \ "@to").text      
    }
    actionNode
    
  }
}