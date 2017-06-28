package com.example.tolent

import akka.actor.Actor
import akka.actor.ActorLogging

class Worker(t: Task) extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case a: String => 
      if(a == "exception"){
        throw new Exception("出错啦！！！")
      } else {        
    	  log.info(s"work: ${t.name}->收到消息： $a")
      }
  }
  
  override def preStart() = {
    println("prestart")
  }
  override def postStop() = {
    println("postStop")
  }
  
  override def preRestart(reason: Throwable, message: Option[Any]){
     println("preRestart")
  }
  
  override def postRestart(reason: Throwable){
    println("postRestart")
  }
  
}
