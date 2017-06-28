package com.kent.distri

import akka.actor.Actor

class Simple extends Actor {
  def receive: Actor.Receive = {
      case m => println(s"received $m!")
    }
}