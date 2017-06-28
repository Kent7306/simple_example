package com.kent.remote

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import scala.concurrent.duration._

object FrontendMain extends App{
  import Producer._
  
  val config = ConfigFactory.load("frontend")
  
  val system = ActorSystem("frontend",config)
  
  val p1 = system.actorOf(Producer.props,"p1")
 // val p2 = system.actorOf(Producer.props,"p2")
  
  p1 ! Start()
  //p2 ! Start()
}