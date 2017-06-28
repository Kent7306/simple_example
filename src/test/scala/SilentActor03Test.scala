import akka.testkit.TestKit
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import org.scalatest.MustMatchers
import akka.actor.ActorRef
import akka.actor.Actor
import SilentActorProto2.GetState
import akka.actor.Props
import SilentActorProto2.SilentMessage
import akka.actor.ActorLogging


class SilentActor03Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll{
  
  "A Silent Actor" must {
    "change internal state when it receives a message, multi" in {
      import SilentActorProto3._
      val silentActor = system.actorOf(Props[SilentActor3], "s3")
      silentActor ! SilentMessage3("whisper1")
      silentActor ! SilentMessage3("whisper2")
      silentActor ! SilentMessage3("whisper3")
      silentActor ! GetState(testActor)
      Thread.sleep(1000)
      
      expectMsg(Vector[String]("whisper1","whisper2","whisper3"))
    }
  }
}

object SilentActorProto3 {
  case class SilentMessage3(data: String)
  case class GetSate(receiver: ActorRef)
}

class SilentActor3 extends Actor with ActorLogging{
	import SilentActorProto3._
  var internalState = Vector[String]()

  def receive: Actor.Receive = {
    case SilentMessage3(data) => 
      internalState = internalState :+ data
      log.info(internalState.toString())
    case GetState(receiver) =>
      receiver ! internalState
  }
  
}