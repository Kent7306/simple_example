package com.kent.tolent.restart

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.ActorSystem
import akka.actor.SupervisorStrategy._

object Test extends App{
   val system = ActorSystem("test")
  val p = system.actorOf(Props[Parent],"p")
}

class Parent extends Actor {
  var user = new User("kent",20)
  override def supervisorStrategy = OneForOneStrategy(){
    case _:Exception => println("*******Restart*******");Restart
  }
  val child = context.actorOf(Props(new Child(user)))
  Thread.sleep(2000)
  user = new User("kent222",30)
  
  def receive: Actor.Receive = {
    case _ =>
  }
}

class Child(user: User) extends Actor {
  println("child init...")
  Thread.sleep(5000)
  self ! "打印。。。"
  def receive: Actor.Receive = {
    case x: String => println(x + user);throw new Exception("出错啦")
  }
}

class User(name: String, age: Int){
  override def toString:String = {
    s"${name}  ${age}"
  }
}