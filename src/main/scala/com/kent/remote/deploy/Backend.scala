package com.kent.remote.deploy

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object Backend extends App{
  val config = ConfigFactory.load("backend")
  val system = ActorSystem.create("backend", config)
    
}