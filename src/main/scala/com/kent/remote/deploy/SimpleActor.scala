package com.kent.remote.deploy

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props

object SimpleActor{
  def props = Props(new SimpleActor())
}

class SimpleActor extends Actor with ActorLogging{
  def receive: Actor.Receive = {
    case msg:Any => 
      val str = msg.asInstanceOf[String]
      log.info(str)
  }
}