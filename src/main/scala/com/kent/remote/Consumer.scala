package com.kent.remote

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.util.Timeout
import com.kent.remote.Producer.Ensure

object Consumer{
  def props = Props(new Consumer())
  
  case class Record(msg: String)
}

class Consumer extends Actor with ActorLogging{
  import Consumer._
  def receive: Actor.Receive = {
    case Record(msg: String) => 
      log.info(msg)
      sender ! Ensure()
  }
}