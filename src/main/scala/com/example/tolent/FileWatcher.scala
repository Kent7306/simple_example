package com.example.tolent

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Terminated
import akka.actor.PoisonPill
import scala.io.Source
import java.io.File
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._


/**
 * 监管器
 */
class FileWatcherSupervisor(logProSupervisorProps: Props) extends Actor with ActorLogging {
  log.info("FileWatcherSupervisor init...")
  override def supervisorStrategy = OneForOneStrategy(){
    case _:Exception => println("*******Restart*******");Restart
  }
  
  
  var logProcessorSuperMap = Map[String, ActorRef]()
  
  import FileWatcherProtocal._
  import LogProcessorProtocal._
  def receive: Actor.Receive = {
    case Terminated(logProSuper) =>
      logProcessorSuperMap.foreach(t => if(t._2 == logProSuper){logProcessorSuperMap - t._1})
      log.error("***********关闭文件啦")
    case AddFile(path: String) => 
      val file = new File(path)
      if(file.exists()){
        val logProSuper = context.actorOf(logProSupervisorProps,logProcessorSuperMap.size.toString())
    	  context.watch(logProSuper)
        logProcessorSuperMap += (path -> logProSuper)
    	  logProSuper ! HandleLogFile(file)
    	  
        log.info("the file is running")
      }else{
        log.error("the file is not exist!")
      }
  }
}

object FileWatcherProtocal {
  case class AddFile(path: String)
  case class DelFile(path: String)
  case class StopFileWatcher()
}