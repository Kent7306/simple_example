package com.kent.remote

import akka.actor._
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.util.Timeout
import akka.actor.Props
import akka.actor.ActorRef
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await

object Producer{
  def props =   Props(new Producer())
  
  def consumerPath = "akka.tcp://backend@0.0.0.0:2551/user/consumer"
  case class Handle(msg: String)
  case class Start()
  case class End()
  case class Ensure()
  case class GetStatus()
  case class Status(msg: String)
}

class Producer extends Actor with ActorLogging {
  import Producer._
  import Consumer._
  def receive: Actor.Receive = {
    case Handle(msg: String) => log.info("handle")
    case Start() => produce()
    case End() => ???
  }
  
  def produce(){
    implicit def int2String(num: Int) = num.toString()
    //val consumer = context.actorSelection(consumerPath)
    val consumer = context.actorOf(Props(new RemoteLookupProxy(consumerPath)),"proxy")
    implicit val timeout = Timeout(5 seconds)
    for(i <- 1 to 100){
      Thread.sleep(5000)
      val f1 = ask(consumer, GetStatus()).mapTo[Status]
      val r1 = Await.result(f1, 3 seconds)
      if(r1.msg == "active"){
    	  val f2 = ask(consumer, Record(i)).mapTo[Ensure]
    	  val r2 = Await.result(f2, 3 seconds)
        log.info(s"message: $i is ensure!!!!!")
      }else{
        log.info(s"the lookup is identified!!")
      }
      
    }
  }
  
  
}