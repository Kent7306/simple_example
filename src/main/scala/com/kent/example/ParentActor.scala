package com.kent.example

import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.PoisonPill

class ParentActor extends Actor with ActorLogging {
  var child: ActorRef = context.actorOf(Props[SonActor], "son")
  val sa = context.child("son")
  log.info("-------------");
  val pa = context.actorSelection("son1")
  Thread.sleep(500)
  log.info("-------------");
  //pa ! "kent"
  log.info("--------"+pa)
  
  def receive: Actor.Receive = {
    case "stop_sons" => child ! PoisonPill
    case x => child ! x;
              log.info("received " + x);
  }
  
  override def postStop(): Unit = {
    log.info("post stop in ParentActor")
  }
}