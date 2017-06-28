package com.kent.distri

object FrontApp extends App{
  val conf = """
    akka {
        actor {
            provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
            enabled-transports = ["akka.remote.netty.tcp"]
            netty.tcp {
                hostname = "0.0.0.0"
                port = 2552
            }
        }
    }
  """
  import com.typesafe.config._
  import akka.actor._
  val config = ConfigFactory.parseString(conf)
  val frontend = ActorSystem("frontend", config)
  
  val path = "akka.tcp://backend@0.0.0.0:2551/user/simple"
  val simple = frontend.actorSelection(path)
  simple ! "Hello Remote World!"
  
}