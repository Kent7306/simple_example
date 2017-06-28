package com.kent.dispatcher

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import com.typesafe.config.ConfigFactory


object DispatcherTest extends App{
  val defaultConf = ConfigFactory.load()
  val hostConf = "akka.remote.netty.tcp.hostname=127.0.0.1"
  val portConf = "akka.remote.netty.tcp.port=2751"
  
  // 创建一个Config对象
  val config = ConfigFactory.parseString(portConf)
      .withFallback(ConfigFactory.parseString(hostConf))
      .withFallback(ConfigFactory.parseString("akka.cluster.roles = [master]"))
      .withFallback(defaultConf)
  
  // 创建一个ActorSystem实例
  val system = ActorSystem("akkaflow")
  val m = system.actorOf(Props[Master],"master")
  m ! "start"
}

class Master extends ClusterRole {
  def receive: Actor.Receive = {
    case "start" => allocateJob(30)
  }
  def allocateJob(n: Int){
    (1 to n).map{ x =>
      val w = context.actorOf(Props[Worker],s"w$x")
      //w ! "start"
    }
  }
}

class Worker extends Actor {
  def receive: Actor.Receive = {
    case "start" => 
      println(self.path.name+"开始")
      (1 to 2).map{
        x => Thread.sleep(2000);println(self.path.name+"---"+x)
      }
      println(self.path.name+"结束")
  }
}