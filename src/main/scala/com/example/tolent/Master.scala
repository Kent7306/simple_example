package com.example.tolent

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import akka.actor.Props

class Master extends Actor with ActorLogging {
  override def supervisorStrategy = OneForOneStrategy(){
    case _: Exception => println("****Restart");Restart
  }
  val tt = new Task("tttt", 12)
  
  val child = context.actorOf(Props(new Worker(tt)),"tt")
  
  def receive: Actor.Receive = {
    case a: String => println(s"*********$a");child ! a
    }
}