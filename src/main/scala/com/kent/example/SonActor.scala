package com.kent.example

import akka.actor.ActorLogging
import akka.actor.Actor

class SonActor extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case "test" => log.info("received test")
    case "kent" => log.info("kent")
    case _ => log.info("received unkown")
  }
  
  override def postStop(){
    log.info("postStop in SonActor")
  }
}