package com.kent.remote.deploy

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props

object FrontActor{
  def props = Props(new FrontActor())
}

class FrontActor extends Actor with ActorLogging {
  val b1 = context.actorOf(SimpleActor.props,"b1")
  def receive: Actor.Receive = {
    case msg: Any => 
      val str = msg.asInstanceOf[String]
      log.info(s"FrontActor: $str")
      b1 ! msg
  }
}