import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpec
import org.scalatest.MustMatchers
import org.scalatest.WordSpecLike
import akka.actor.Actor
import scala.util.Success


class SilentActor01Test extends TestKit(ActorSystem("testSystem")) 
with WordSpecLike 
with MustMatchers 
with StopSystemAfterAll{
  "a silent actor" must {
    "change state when it receives a message, single threaded" in {
    	Success("not implemented yet")      
    }
    "change state when it receives a message, multi-threaded" in {
      Success("not implemented yet")  
    }
  }
}

class SilentActor extends Actor {
  def receive = {
    case msg => 
  }
}