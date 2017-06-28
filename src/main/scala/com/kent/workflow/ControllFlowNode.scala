package com.kent.workflow
import com.kent.workflow.Node.Status._

abstract class ControllFlowNode(private var _name:String) extends Node(_name) {
  def ifCanExecuted(wf: WorkFlow): Boolean = true
  

  def start(wfa: WorkflowActor): Boolean = {
		this.execute()
		this.terminate(wfa)
  }
  def execute(): Boolean = {
    this.status = RUNNING
    true
  }
  def terminate(wfa: WorkflowActor): Boolean = {
		  this.status = SUCCESSED
		  val nodes = this.getNextNodes(wfa.wf)
		  nodes.filter { _.ifCanExecuted(wfa.wf) }.foreach { x => wfa.waitingNodes = wfa.waitingNodes.enqueue(x)}
		  true
  }

  def replaceParam(param: Map[String, String]): Boolean = true

}

object ControllFlowNode{
  def apply(node: scala.xml.Node): ControllFlowNode = parseXmlNode(node)
  
  def parseXmlNode(node: scala.xml.Node):ControllFlowNode = {
    node match {
      case <start/> => 
        StartNode(node)
      case <end/> =>
        EndNode(node)
      case <kill>{contents @_*}</kill> =>
			  KillNode(node)
      case <fork>{contents @_*}</fork> =>
        ForkNode(node)
      case <join/> =>
        JoinNode(node)
      case _ =>
        throw new Exception("控制节点不存在:" + node)
   }
  }
}