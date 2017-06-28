package com.kent.workflow

class JoinNode(private var _name: String) extends ControllFlowNode(_name) {
  import com.kent.workflow.Node.Status._
  var to:String = null

  def deepClone(): Node = {
    val jn = JoinNode(name)
    jn.to = to
    jn
  }

  def getNextNodes(wf: WorkFlow): List[Node] = wf.nodeList.filter { _.name == to }.toList
  
  override def ifCanExecuted(wf: WorkFlow): Boolean = {
		var isExcecuted = true
    wf.nodeList.filter { _.isInstanceOf[ActionNode] }.foreach { x => 
      if(x.asInstanceOf[ActionNode].ok == name && x.status != SUCCESSED){
        isExcecuted = false
      }else if(x.asInstanceOf[ActionNode].error == name && x.status != FAILED){
        isExcecuted = false
      }
    }
   wf.nodeList.filter { _.isInstanceOf[ControllFlowNode]}.foreach {
     _ match {
       case n: JoinNode => if(n.to == name && n.status != SUCCESSED) isExcecuted = false
       case n: StartNode => if(n.to == name && n.status != SUCCESSED) isExcecuted = false
       case n: ForkNode => if(n.pathList.contains(name) && n.status != SUCCESSED) isExcecuted = false
     }
   }
   isExcecuted
  }
}

object JoinNode {
  def apply(name: String): JoinNode = new JoinNode(name)
  def apply(node: scala.xml.Node): JoinNode = parseXmlNode(node)
  
  def parseXmlNode(node: scala.xml.Node): JoinNode = {
	  val nameOpt = node.attribute("name")
	  val toOpt = node.attribute("to")
    if((node \ "@name").size != 1){    
  	  throw new Exception("存在join未配置name属性")
    }
    val jn = JoinNode(nameOpt.get.text) 
    jn.to = toOpt.get.text
    jn
  }
}