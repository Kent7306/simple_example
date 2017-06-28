import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
  
object Hello{
	def main(argv: Array[String]){
		val a = "你好啊"
		val str = s"println ${a}"
		println(str)
	}

}