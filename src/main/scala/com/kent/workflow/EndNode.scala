package com.kent.workflow

import com.kent.util.Util
import com.kent.workflow.WorkFlowManager.WorkFlowInstanceExecuteResult
import com.kent.workflow.WorkFlow.WStatus._
import com.kent.workflow.Node.Status._

class EndNode(private var _name: String) extends ControllFlowNode(_name) {
  def deepClone(): Node = {
    EndNode(name)
  }

  def getNextNodes(wf: WorkFlow): List[Node] = {
    return List()
  }
  
  override def terminate(wfa: WorkflowActor): Boolean = {
    this.status = SUCCESSED
    wfa.wf.endTime = Util.nowDate
    println("workflow名称："+wfa.wf.name+"执行完毕."+"actor名称: "+ wfa.wf.actorName)
    wfa.wf.status = W_SUCCESSED
    wfa.workflowManageAcotrRef ! WorkFlowInstanceExecuteResult(wfa.wf)
    wfa.context.stop(wfa.self)
    true
  }
  
}

object EndNode {
  def apply(name: String): EndNode = new EndNode(name)
  def apply(node: scala.xml.Node): EndNode = parseXmlNode(node)
  /**
   * 解析xml为一个对象
   */
  def parseXmlNode(node: scala.xml.Node): EndNode = {
    val nameOpt = node.attribute("name")
    if(nameOpt == None){
      throw new Exception("节点<end/>未配置name属性")
    }
    EndNode(nameOpt.get.text)
  }
}