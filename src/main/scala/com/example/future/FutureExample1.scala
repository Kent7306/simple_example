package com.example.future

import scala.util.Random
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureExample1 extends App{
	case class Water(temperature: Int)
	case class GrindingException(msg: String) extends Exception(msg)
	case class FrothingException(msg: String) extends Exception(msg)
	case class WaterBoilingException(msg: String) extends Exception(msg)
	case class BrewingException(msg: String) extends Exception(msg)
	
	//搅碎咖啡豆
	def grind(beans: String): Future[String] = Future{
		println("start grinding")
		Thread.sleep(Random.nextInt(2000))
		if(beans == "baked beans") throw GrindingException("are you joking")
		println("finish grinding")
		s"ground coffee of $beans"
	}
	//烧水
	def heatWater(water: Water):Future[Water] = Future{
		println ("heating the water now")
		Thread.sleep(Random.nextInt(2000))
		println("hot, it's hot!")
		water.copy(temperature = 85)
	}
	//起奶泡
	def frothMilk(milk: String):Future[String] = Future{
		println("milk frothing system engaged!")
		Thread.sleep(Random.nextInt(2000))
		println("shutting down milk frothing system")
		s"froth $milk"
	}
	//调制咖啡
	def brew(coffee: String, heatedWater: Water):Future[String] = Future{
		println("happy brew :)")
		Thread.sleep(Random.nextInt(2000))
		println("it's brewed!")
		"espresso"
	}
	
	/*val tempreatureOkay: Future[Boolean] = heatWater(Water(25)) map { water =>
	   println("we're in the future!")
	   (80 to 85) contains (water.temperature)
	}*/
	
	def tempreatureOkay(water: Water): Future[Boolean] = Future{
		println("we're in the future!")
	   (80 to 85) contains (water.temperature)
	}
	
	// val flatFuture:Future[Boolean] = heatWater(Water(25)) flatMap {
		// water => tempreatureOkay(water)
	// }
	// val flatFuture2:Future[String] = flatFuture map {
		// is_Contain => println("&*&*&*&*&*&")
		// "&&&&&&"
	// }
	
	val acceptable:Future[Boolean] = for {
		heatwater <- heatWater(Water(25))
		okay <- tempreatureOkay(heatwater)
	} yield okay
	
	/*val tempreatureOkay.onSuccess{
		 case isOk => println("wawawawa,get hot water!")
	}*/
	
	/*grind("arabica beans").onSuccess { case ground =>
	   println("okay, got my ground coffee: " + ground)
	 }*/
	 /*import scala.util.{Success, Failure}
	 grind("baked beans").onComplete{
		case Success(ground: String) => println(s"got my $ground")
		case Failure(ex : Exception) => println(s"This grinder needs a replacement, seriously! " + ex.getMessage())
	 }*/
	 
	 Thread.sleep(10000);
	 println("end")
	
	
}