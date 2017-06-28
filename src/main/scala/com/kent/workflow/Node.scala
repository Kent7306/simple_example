package com.kent.workflow

import java.util.Date

abstract class Node(private var _name: String) extends DeepCloneable[Node] {
	  import com.kent.workflow.Node.Status._
	  var id: String = _
    var status: Status = PREP
    def name = _name
    var startTime: Date= _
    var endTime: Date = _
    var classType:Class[_ <: com.kent.workflow.Node] = this.getClass
    
    var actionExecutedMsg: String = _
    //def start() = Unit
    def ifCanExecuted(wf: WorkFlow): Boolean
    def start(wfa :WorkflowActor):Boolean
    def execute(): Boolean
    def terminate(wfa :WorkflowActor): Boolean
    def deepClone(): Node
    def getNextNodes(wf: WorkFlow): List[Node]
	  def replaceParam(param: Map[String, String]): Boolean
	  //def execute():Boolean
	  
	  override def toString(): String = {
	    var str = "  "+this.getClass.getName + "(\n"
	    str = str + s"    id = ${id},\n"
	    str = str + s"    name = ${name},\n"
	    str = str + s"    status = ${status},\n"
	    str = str + s"    startTime = ${startTime},\n"
	    str = str + s"    endTime = ${endTime})\n"
	    str
	  }
	  
}

object Node {
  def apply(node: scala.xml.Node): Node = parseXmlNode(node)
  
  def parseXmlNode(node: scala.xml.Node): Node = {
    node match {
      case <action>{content @ _*}</action> => ActionNode(node)
      case _ => ControllFlowNode(node)
    }
  }
  
  object Status extends Enumeration { 
    type Status = Value
    val PREP, RUNNING, SUSPENDED, SUCCESSED,FAILED,KILLED = Value
  }
  object NodeTagType  extends Enumeration{
    type  NodeTagType = Value
    val KILL = Value("kill")
    val START = Value("start")
    val END = Value("end")
    val JOIN = Value("join")
    val FORK = Value("fork")
    val HOST_SCRIPT = Value("host_cript")
    val SUB_WORKFLOW = Value("sub_workflow")
    val ACTION = Value("action")
  }
}

