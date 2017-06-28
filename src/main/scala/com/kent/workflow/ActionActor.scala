package com.kent.workflow

import akka.actor.ActorLogging
import akka.actor.Actor

import com.kent.workflow.WorkflowActor._
import com.kent.workflow.ActionActor._
import com.kent.workflow.Node.Status._
class ActionActor(actionNode: ActionNode) extends Actor with ActorLogging {
  def receive: Actor.Receive = {
    case Start() => 
      val executedStatus = if(actionNode.execute()) SUCCESSED else FAILED
      sender ! ActionExecuteResult(executedStatus, "节点执行成功")
    case Kill() =>
      // ???
      sender ! ActionExecuteResult(KILLED, "节点被kill掉了")
      context.stop(self)
  }
}

object ActionActor{
  case class ActionExecuteResult(status: Status, msg: String)
}