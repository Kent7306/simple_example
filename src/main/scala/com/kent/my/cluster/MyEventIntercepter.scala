package com.kent.my.cluster

import akka.cluster.ClusterEvent.MemberUp
import akka.actor.Actor
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.ClusterEvent.MemberEvent
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

class MyEventInterceptor extends MyClusterRoleWorker {
  def receive: Actor.Receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case state: CurrentClusterState =>
       ???
    case UnreachableMember(member) =>
      log.info("Member detected as Unreachable: {}", member.address)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}", member.address, previousStatus)
    case _: MemberEvent =>
      
    case NginxRecord(sourceHost, eventCode,line) =>
      log.info("Intercepter receive: {}",eventCode)
  }
}

object MyEventInterceptor extends App {
  Seq("2851","2852").foreach { port => 
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port="+port)
      .withFallback(ConfigFactory.parseString("akka.cluster.roles=[interceptor]"))
      .withFallback(ConfigFactory.load())
    val system = ActorSystem("my-event-cluster-system", config)
    val processingActor = system.actorOf(Props[MyEventInterceptor], name="myInterceptingActor")
    system.log.info("Procesing Actor: " + processingActor)
  }
}