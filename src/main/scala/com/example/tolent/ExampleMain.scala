package com.example.tolent

import akka.actor.ActorSystem
import akka.actor.Props

object ExampleMain extends App{
  val s = ActorSystem("system")
  val m = s.actorOf(Props[Master],"master")
  Thread.sleep(2000)
  m ! "yoyoyoyo"
  Thread.sleep(5000)
  m ! "exception"
  
  Thread.sleep(5000)
  m ! "new...."
}