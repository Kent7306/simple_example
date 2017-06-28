package com.kent.workflow

import com.kent.workflow.WorkFlow.WStatus._
import com.kent.coordinate.ParamHandler
import java.util.Date
import com.kent.coordinate.ParamHandler

class KillNode(private var _name: String) extends ControllFlowNode(_name) {
  private var _msg:String = null
  def msg = _msg
  def msg_=(msg: String) = this._msg = msg

  def deepClone(): Node = {
    val kn = new KillNode(this._name)
    kn.msg = _msg
    kn
  }

  def getNextNodes(wf: WorkFlow): List[Node] = List()
  
  override def terminate(wfa: WorkflowActor): Boolean = {
    println("KILL! 执行workflow名称："+wfa.wf.name+"执行完毕."+"actor名称: "+ wfa.wf.actorName)
    wfa.wf.status = W_KILLED
    WorkflowActor.killRunningNodeActors(wfa)
    true
  }

  override def replaceParam(param: Map[String, String]): Boolean = {
    this._msg = ParamHandler(new Date()).getValue(msg)
    true
  }
}

object KillNode {
  def apply(name: String): KillNode = new KillNode(name)
  def apply(node: scala.xml.Node): KillNode = parseXmlNode(node)
  /**
   * 解析xml为一个对象
   */
  def parseXmlNode(node: scala.xml.Node): KillNode = {
      val nameOpt = node.attribute("name")
      val msg = (node \ "message").text
      
      if(nameOpt == None){
        throw new Exception("节点<kill/>未配置name属性")
      }
      val kn = KillNode(nameOpt.get.text)
      kn.msg = msg
      kn
  }
}