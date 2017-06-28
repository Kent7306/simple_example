package com.example.tolent

import akka.actor.Props
import akka.actor.ActorSystem

object Main extends App{
  import FileWatcherProtocal._
  
  val lineHandlerSuperPros = Props(new LineHandlerSupervisor())
  val logProcessorSuperProps = Props(new LogProcessorSupervisor(lineHandlerSuperPros))
  val fileWatcherSuperProps = Props(new FileWatcherSupervisor(logProcessorSuperProps))
  
  val system = ActorSystem("kent")
  val fileWatcherSuper = system.actorOf(fileWatcherSuperProps,"fileWatcherSuper")
  
  Thread.sleep(2000)
  
  fileWatcherSuper ! AddFile("F://test.txt")
  
  fileWatcherSuper ! AddFile("F://test.txt")
}