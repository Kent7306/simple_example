package com.kent.example

import akka.actor.ActorSystem
import akka.actor.Props

object ActorStop extends App{
  val system = ActorSystem("kentSystem")
  val log = system.log
  
  val pa = system.actorOf(Props[ParentActor], "parent")
  log.info("开始往parent发送消息")
  pa ! "test"
 // pa ! 123
  //Thread.sleep(1000)
  pa ! "stop_sons"
 // system.stop(pa);
  Thread.sleep(5000)
  log.info("stop......")
  system.shutdown()  
}