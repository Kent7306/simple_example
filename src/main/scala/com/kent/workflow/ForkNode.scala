package com.kent.workflow

import com.kent.workflow.Node.Status._
class ForkNode(private var _name: String) extends ControllFlowNode(_name) {
  var pathList: List[String] = List()

  def deepClone(): Node = {
    val fn = ForkNode(name)
    fn.pathList = pathList.map {x => x }.toList
    fn
  }

  def getNextNodes(wf: WorkFlow): List[Node] = wf.nodeList.filter { pathList.contains(_) }.toList
}


object ForkNode {
  def apply(name: String): ForkNode = new ForkNode(name)
  def apply(node: scala.xml.Node): ForkNode = parseXmlNode(node)
  
  def parseXmlNode(node: scala.xml.Node): ForkNode = {
	  val nameOpt = node.attribute("name")
		val pathList = (node \ "path").map { x => x.attribute("start").get.text }.toList
    if((node \ "@name").size != 1){    
  	  throw new Exception("存在fork未配置name属性")
    }else if(pathList.size <= 0){
       throw new Exception("[fork] "+nameOpt.get.text+":-->[error]:未配置path子标签")
    }
    val fn = ForkNode(nameOpt.get.text)
    fn.pathList = pathList
    fn
  }
}