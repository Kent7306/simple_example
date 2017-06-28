package com.example.future
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.util.control.NonFatal

object SimpleExample extends App{
  val futureFail = Future {
    throw new Exception("error!")
    "hello future"
  }
  futureFail.foreach { println _ }
  futureFail.onComplete { 
    case Success(value) => println(value) ;
    case Failure(NonFatal(e)) => println(e)
  }
  
  println("===========")
  
  val res = Future {
    "1"
  }
  res map {
    "res="+_
  } foreach { 
    x => println("that" + x)
  }
  //recover 
  val f = Future {
    throw new Exception("nihao")
    1
  } map {
    x => "res=" + x;println(x)
  } recover {
    case e: Exception => 2
  } onComplete {
    case Success(r) => println(r)
    case Failure(NonFatal(e)) => e.printStackTrace()
  }
  
  //firstCompletedOf
 def getResult1() = {
  Thread.sleep(3000)
   "result1"
 }
 def getResult2() = {
   Thread.sleep(1000)
   "result2"
 }
 
 def fgr1 = Future { Some(getResult1()) }.recover{case e:Exception => None}
 def fgr2 = Future { Some(getResult2()) }.recover{case e:Exception => None}
 fgr1.zip(fgr2).map{case (x,y) => println(x.get,y.get)}
 
/* val list = List(fgr1,fgr2)
 val fr = Future.firstCompletedOf(list) map { x => println(x);x } onComplete {
    case Success(r) => println(r+"****")
    case Failure(NonFatal(e)) => e.printStackTrace()
  }*/
 Thread.sleep(5000)
  
}