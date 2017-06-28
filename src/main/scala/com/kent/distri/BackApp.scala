package com.kent.distri
import akka.actor._
import com.typesafe.config.ConfigFactory

object BackApp extends App{
  val conf = """
    akka {
        actor {
            provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
            enabled-transports = ["akka.remote.netty.tcp"]
            netty.tcp {
                hostname = "0.0.0.0"
                port = 2551
            }
        }
    }
  """
  val config = ConfigFactory.parseString(conf)
  val backend = ActorSystem("backend", config)
  backend.actorOf(Props[Simple],"simple")
  
}