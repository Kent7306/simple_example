package com.kent.workflow

import scala.xml.Text
import akka.actor.Props

import com.kent.workflow.Node.Status._
import com.kent.coordinate.ParamHandler
import java.util.Date

class HostScriptActionNode(private var _name: String) extends ActionNode(_name) {
  this.classType = this.getClass
  var host: String = _
  var script: String = _

  def deepClone(): Node = {
    val hsa = HostScriptActionNode(name)
    hsa.host = host
    hsa.script = script
    this.deepCloneAssist(hsa)
  }

  override def execute(): Boolean = {
    println(s"****${host}****${script}***********")
    true
  }

  def replaceParam(param: Map[String, String]): Boolean = {
    host = ParamHandler(new Date()).getValue(host)
    script = ParamHandler(new Date()).getValue(script)
    true
  }

}

object HostScriptActionNode {
  def apply(name: String): HostScriptActionNode = new HostScriptActionNode(name)
  def apply(name:String, node: scala.xml.Node): HostScriptActionNode = parseXmlNode(name, node)
  
  def parseXmlNode(name: String, node: scala.xml.Node): HostScriptActionNode = {
    val host = (node \ "host")(0).text
    val script = (node \ "script")(0).text
    
	  val hsan = HostScriptActionNode(name)
	  hsan.host = host
	  hsan.script = script
	  hsan
  }
}