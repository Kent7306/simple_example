package com.kent.workflow

import scala.xml.XML
import scala.util.Random
import java.util.UUID
import com.kent.util.UUIDGenerator
import java.util.Date

class WorkFlow(private var _name:String) extends DeepCloneable[WorkFlow] {
	import com.kent.workflow.WorkFlow.WStatus._
  var id: String = _
  def name = _name
  def actorName = s"wf_${id}_${name}"
  var nodeList:List[Node] = List()
  var params:Map[String, String] = Map()
  var startTime: Date = _
  var endTime: Date = _
  var status: WStatus = W_PREP  
  
  def init(){
  }
  /**
   * 深度克隆
   */
  def deepClone(): WorkFlow = {
    val cpwf = new WorkFlow(this._name)
    cpwf.id = UUIDGenerator.produce8UUID
    cpwf.nodeList = nodeList.map { x => x.deepClone();x.id=cpwf.id;x }.toList
    cpwf
  }
  /**
   * 得到工作流的开始节点
   */
  def getStartNode():Option[Node] = {
	  val sn = nodeList.filter { _.isInstanceOf[StartNode] }.toList
	  if(sn.size == 1) Some(sn(0)) else None
  }
  
  override def toString: String = {
    var str = this.getClass().getName + "(\n"
    str = str + s"  id = ${id},\n"
    str = str + s"  name = ${name},\n"
    str = str + s"  actorName = ${actorName},\n"
    str = str + s"  status = ${status},\n"
    str = str + s"  startTime = ${startTime},\n"
    str = str + s"  endTime = ${endTime},\n"
    this.nodeList.foreach { x => str = str + x.toString() }
    str = str + s")"
    str
  }
}


object WorkFlow{
  def apply(content: String): WorkFlow = WorkFlow(XML.loadString(content))
  def apply(node: scala.xml.Node): WorkFlow = parseXmlNode(node)
  /**
   * 解析xml为一个对象
   */
  def parseXmlNode(node: scala.xml.Node): WorkFlow = {
      val nameOpt = node.attribute("name")
      if(nameOpt == None) throw new Exception("节点<work-flow/>未配置name属性")
      val wf = new WorkFlow(nameOpt.get.text)
    	wf.nodeList = (node \ "_").map(Node(_)).toList
    	wf
  }
  /**
   * 工作流状态
   */
  object WStatus extends Enumeration {
    type WStatus = Value
    val W_PREP, W_RUNNING, W_SUSPENDED, W_SUCCESSED, W_FAILED, W_KILLED = Value
  }
  def main(args: Array[String]): Unit = {
    val xmlObj = XML.loadFile("J:/wf.xml")
    val wf = WorkFlow(xmlObj)
    wf.nodeList.foreach { x => println(x.name) }
  }
  
}