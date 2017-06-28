package com.example.future

import scala.util.Random
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureExample2 extends App{
	
	def turnOnCompute():Future[Boolean] = Future{
		Thread.sleep(Random.nextInt(5000))
		println("computer is on")
		true
	}
	
	def openPage(filename: String):Future[Boolean] = Future{
		Thread.sleep(Random.nextInt(5000))
		println(s"page[${filename}] is open")
		true
	}
	def turnOnAirRefresh(tempeature: Int):Future[Int] = Future{
		var tempeature2:Int = tempeature
		if(tempeature2 > 32){
			println("the air-refresh is turnning on")
			Thread.sleep(Random.nextInt(2000))
			tempeature2 = 28
		}
		println(s"the tempeature is ${tempeature2}")
		tempeature2
	}
	
	def enjoinFilm(isPageOpen:Boolean, tempeature:Int){
		println("I like the film....")
	}
	
	for {
		isComputerOn <- turnOnCompute()
		isPageOpen <- openPage("你好啊")
		tempeature <- turnOnAirRefresh(36)
	} yield enjoinFilm(isPageOpen,tempeature)
	
	Thread.sleep(15000)
}