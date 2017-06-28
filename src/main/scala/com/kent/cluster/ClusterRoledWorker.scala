package com.kent.cluster

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.MemberEvent
import akka.cluster.ClusterEvent.InitialStateAsEvents

import akka.cluster.Member
import akka.actor.ActorPath

abstract class ClusterRoledWorker extends Actor with ActorLogging {

  // 创建一个Cluster实例
  val cluster = Cluster(context.system) 
  // 用来缓存下游注册过来的子系统ActorRef
  var workers = IndexedSeq.empty[ActorRef] 

  override def preStart(): Unit = {
    // 订阅集群事件
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberUp], classOf[UnreachableMember], classOf[MemberEvent])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  /**
   * 下游子系统节点发送注册消息
   */
  def register(member: Member, createPath: (Member) => ActorPath): Unit = { 
    val actorPath = createPath(member)
    log.info("Actor path: " + actorPath)
    val actorSelection = context.actorSelection(actorPath)
    log.info(actorSelection.toString()+"____________________");
    actorSelection ! Registration
  }

  def receive: Actor.Receive = {
    ???
  }
}

object Registration extends Serializable

trait EventMessage extends Serializable
case class RawNginxRecord(sourceHost: String, line: String) extends EventMessage
case class NginxRecord(sourceHost: String, eventCode: String, line: String) extends EventMessage
case class FilteredRecord(sourceHost: String, eventCode: String, line: String, logDate: String, realIp: String) extends EventMessage