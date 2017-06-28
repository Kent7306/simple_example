package com.kent.workflow

import scala.xml.XML
import scala.xml.Elem

object XmlAnalyse extends App{
	//implicit def NodeTagTypeToString(a: NodeTagType.Value):String = a.toString()
  def loadFile(filePath: String): Unit = {
    val xmlObj = XML.loadFile(filePath)
    val nodeList = (xmlObj \ "_").map(Node(_)).toList
    
    nodeList.foreach { x => println(x.name) }
  }
  
  loadFile("J:/wf.xml")
  
}