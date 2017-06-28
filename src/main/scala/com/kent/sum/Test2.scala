package com.kent.sum

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorSystem
import akka.pattern.{ ask, pipe }
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future

object Test2 extends App{
  case class DoSum(sNum: Int, eNum: Int, workerNum: Int)
  case class SubSum(nums:List[Int])
  case class Result(num: Int)
  
  val system = ActorSystem("sum")
  val master = system.actorOf(Props[Master2])
  master ! DoSum(0,1000,4)
}

class Master2 extends Actor {
  implicit val timeout = Timeout(20 seconds)
  import com.kent.sum.Test2._
  
  def receive: Actor.Receive = {
    case DoSum(sn, en, wn) =>
      val lists = (sn to en).groupBy(_ % 5).map{case (x,y) => y.toList}
      val resultsF = lists.map { x => 
        val worker = context.actorOf(Props[Worker2])
        (worker ? SubSum(x)).mapTo[Result]
      }.toList
      Future.sequence(resultsF).map{}
      
  }
}

class Worker2 extends Actor {
  import com.kent.sum.Test2._
  def receive: Actor.Receive = {
    case SubSum(list) => 
      val num = list.sum
      sender ! Result(num)
  }
}