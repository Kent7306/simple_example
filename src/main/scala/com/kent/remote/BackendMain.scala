package com.kent.remote

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object BackendMain extends App{
  val config = ConfigFactory.load("backend")
  val system = ActorSystem("backend",config)
  
  system.actorOf(Consumer.props,"consumer")
}