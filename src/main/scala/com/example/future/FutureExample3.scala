package com.example.future

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal
import akka.util.Timeout

object FutureExample3 extends App {
  
/*  val a = Future {
    Thread.sleep(3000)
    println("a")
    "a"
  }
  val b = Future {
    Thread.sleep(1000)
    println("b")
    "b"
  }
  val c = for{
    ra <- a
    rb <- b
  } yield (ra,rb)
  
  c.foreach(println(_))
  
  Thread.sleep(10000)
  println("hello2") */
  
/*  val listF = List.fill(100)(Future{ Thread.sleep(1000);println("a");"a"})
  val f2 = Future.sequence(listF)
  f2.foreach { x => println(x) }*/
  
  /*val futureList = Future.traverse((1 to 100).toList)(x => Future(x * 2 - 1))
  val oddSum = futureList.map(_.sum)
  oddSum foreach println*/
  import scala.concurrent.duration._
  implicit val timeout = Timeout(2 seconds)
  val f = Future {
    throw new Exception("nihao")
    1
  } map {
    x => "res=" + x;println(x)
  } recover {
    case e: Exception => println("出错啦");2
  } onComplete {
    case Success(r) => println(r)
    case Failure(NonFatal(e)) => e.printStackTrace()
  }
  
  val f2 = Future{
    "xxxx"
  }
  val f3 = f2.map { x => x +"yyyy" }
  
  Thread.sleep(30000)
}