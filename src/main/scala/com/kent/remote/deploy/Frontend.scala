package com.kent.remote.deploy

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Deploy
import akka.remote.RemoteScope
import akka.actor.AddressFromURIString

object Frontend extends App{
  val config = ConfigFactory.load("front-remote_deploy")
  val system = ActorSystem.create("frontend", config)
  val s1 = system.actorOf(FrontActor.props,"f")
  
  for(i <- 1 to 100){
    Thread.sleep(5000)
	  s1 ! s"hello, simple actor! $i"    
  }
  
  /*val system = ActorSystem.create("frontend")
  
  val props = SimpleActor.props.withDeploy(
    Deploy(scope = RemoteScope(
        AddressFromURIString("akka.tcp://backend@0.0.0.0:2551")))    
  )
  val s1 = system.actorOf(props,"b1")
  s1 ! "hello, simple actor!"*/
}