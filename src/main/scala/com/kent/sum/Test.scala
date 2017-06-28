package com.kent.sum

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorSystem


object Test extends App{
  case class DoSum(sNum: Int, eNum: Int, workerNum: Int)
  case class SubSum(nums:List[Int])
  case class Result(num: Int)
  
  val system = ActorSystem("sum")
  val master = system.actorOf(Props[Master])
  master ! DoSum(0,1000,4)
}

class Master extends Actor {
  import com.kent.sum.Test._
  var resultNums = List[Int]()
  var workCnt: Int = _
  
  def receive: Actor.Receive = {
    case DoSum(sn, en, wn) =>
      val lists = (sn to en).groupBy(_ % 5).map{case (x,y) => y.toList}
      workCnt = lists.size
      lists.foreach { x => 
        val worker = context.actorOf(Props[Worker])
        worker !SubSum(x)
      }
    case Result(n) =>
      resultNums = resultNums :+ n
      if(resultNums.size == workCnt) println(resultNums.sum)
      
  }
}

class Worker extends Actor {
  import com.kent.sum.Test._
  def receive: Actor.Receive = {
    case SubSum(list) => 
      val num = list.sum
      sender ! Result(num)
  }
}