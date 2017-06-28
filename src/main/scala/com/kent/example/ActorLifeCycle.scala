package com.kent.example

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.PoisonPill
import akka.actor.SupervisorStrategy
import akka.actor.SupervisorStrategy._
import akka.actor.OneForOneStrategy
import akka.actor.ActorRef
import akka.actor.Terminated

object ActorLifeCycle extends App{
  val system = ActorSystem.create("kentSystem")
  val myActorRef = system.actorOf(Props[MyActor],"myActor")
  val mywater = system.actorOf(Props(new myWatcher(myActorRef)),"mywatcher")
  myActorRef ! "hello world"
 // myActorRef ! PoisonPill
  
  Thread.sleep(3000)
  system.shutdown();
}

class MyActor extends Actor with ActorLogging{
  
  override def supervisorStrategy = OneForOneStrategy() {
      case _: Exception => Restart
  }
  
  log.info("construturor")
  override def preStart() = {
    log.info("preStart...")
  }
  override def postStop() = {
	  log.info("postStop...")
  }
  override def preRestart(reason: Throwable, message: Option[Any]) = {
    super.preRestart(reason, message)
     val str = reason.getMessage
    log.info(s"preRestart...${str}")
  }
  override def postRestart(reason: Throwable) = {
    super.postRestart(reason)
    log.info("postRestart...")
  }
  
  
  def receive: Actor.Receive = {
    case msg: String =>
      log.info(s"receive ${msg}")
      throw new Exception("Error")
  }
}
/**
 * 监听器
 */
class myWatcher(actor: ActorRef) extends Actor with ActorLogging {
  context.watch(actor)

  def receive: Actor.Receive = {
    case Terminated(actorRef) =>
      log.warning(s"Actor ${actorRef} terminated")
  }
}