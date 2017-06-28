package com.example.tolent

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.ActorLogging
import java.io.File
import scala.io.Source
import akka.actor.SupervisorStrategy._
import akka.actor.OneForOneStrategy

class LogProcessor(lineHandlerSupervisor: ActorRef) extends Actor with ActorLogging {
  import LogProcessorProtocal._
  import LineHandlerProtocal._
  def receive: Actor.Receive = {
    case HandleLogFile(file) => 
      for(line <- Source.fromFile(file).getLines()){
        lineHandlerSupervisor ! Line(line) 
      }
      throw FileEndException(file.getName)
  }
}

class LogProcessorSupervisor(lineHandlerSupervisorProps: Props) extends Actor with ActorLogging {
  println("888888888888888888888888")
  override def supervisorStrategy = OneForOneStrategy(){
    case _: FileEndException => Escalate
    case _: Exception => Stop
  }
  val lineHandlerSuper = context.actorOf(lineHandlerSupervisorProps,"lineHadlerSuper")
  val logProcessor = context.actorOf(Props(new LogProcessor(lineHandlerSuper)),"LogProcessor")
  
  import LogProcessorProtocal._
  def receive: Actor.Receive = {
	  case m => logProcessor forward (m)
  }
}

object LogProcessorProtocal{
  case class HandleLogFile(file: File)
}