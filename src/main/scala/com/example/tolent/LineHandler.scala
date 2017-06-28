package com.example.tolent

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._

class LineHandler extends Actor with ActorLogging{
  import LineHandlerProtocal._
  def receive: Actor.Receive = {
    case Line(line) => log.info(line)
  }
  
}

class LineHandlerSupervisor extends Actor with ActorLogging {
  override def supervisorStrategy = OneForOneStrategy(){
    case _: FileEndException => Escalate
    case _: Exception => Resume
  }
  
  val lineHandler = context.actorOf(Props[LineHandler],"LineHandler")
  
  def receive: Actor.Receive = {
    case m => lineHandler forward m
  }
}

object LineHandlerProtocal {
  case class Line(line: String)
}
case class FileEndException(msg: String) extends Exception(msg)
