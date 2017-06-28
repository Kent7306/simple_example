package com.kent.my.cluster

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.cluster.Cluster
import akka.actor.ActorRef
import akka.cluster.ClusterEvent.InitialStateAsEvents
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberEvent

abstract class MyClusterRoleWorker extends Actor with ActorLogging{
  val cluster = Cluster(context.system)
  var workers = IndexedSeq.empty[ActorRef]
  
  
  override def preStart():Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, 
        classOf[MemberUp], classOf[UnreachableMember], classOf[MemberEvent])
  }
  
  override def postStop():Unit = cluster.unsubscribe(self)
  
}

trait EventMessage extends Serializable
case class RawNginxRecord(sourceHost: String, line: String) extends EventMessage
case class NginxRecord(sourceHost: String, eventCode: String, line: String) extends EventMessage
case class FilteredRecord(sourceHost: String, eventCode: String, line: String, logDate: String, realIp: String) extends EventMessage