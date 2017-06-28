package com.kent.workflow

import com.kent.workflow.Node.Status._
class StartNode(private var _name: String, private var _to: String) extends ControllFlowNode(_name) {
  def to = _to
  override def deepClone():StartNode = {
    StartNode(_name, _to)
  }

  def getNextNodes(wf: WorkFlow): List[Node] = wf.nodeList.filter { _.name == to }.toList
}

object StartNode {
  def apply(name: String, to: String): StartNode = new StartNode(name, to)
  def apply(node: scala.xml.Node): StartNode = parseXmlNode(node)
  /**
   * 解析xml为一个对象
   */
  def parseXmlNode(node: scala.xml.Node): StartNode = {
      val nameOpt = node.attribute("name")
      val toOpt = node.attribute("to")
      
      if(nameOpt == None){
        throw new Exception("节点<start/>未配置name属性")
      }else if(toOpt == None){
        throw new Exception("节点<start/>未配置opt属性")
      }
      val sn = StartNode(nameOpt.get.text, toOpt.get.text)
      sn
  }
}