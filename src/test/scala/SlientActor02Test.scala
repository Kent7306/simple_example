import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import org.scalatest.MustMatchers
import akka.actor.ActorRef
import akka.testkit.TestActorRef
import akka.actor.Actor
import akka.actor.ActorLogging


class SlientActor02Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike 
  with MustMatchers
  with StopSystemAfterAll {
  "A Silent Actor" must {
    "change internal state when it receives a message, single" in {
    	import SilentActorProto2._
      val silentActor = TestActorRef[SilentActor2]
    	silentActor ! SilentMessage("whisper")
    	silentActor.underlyingActor.state must (contain("whisper"))
    }
  }
}

object SilentActorProto2 { 
    case class SilentMessage(data: String)
    case class GetState(receiver: ActorRef)
  }

class SilentActor2 extends Actor with ActorLogging{
  import SilentActorProto2._
  var internalState = Vector[String]()

  def receive: Actor.Receive = {
    case SilentMessage(data) => 
      internalState = internalState :+ data
  }
  
  def state = internalState
  
  
}