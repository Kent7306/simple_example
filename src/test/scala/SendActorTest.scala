import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import org.scalatest.MustMatchers
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorSystem
import scala.util.Random


class SendActorTest extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll{
    "A Sending Actor" must {
      "send a message to another actor when it has finished processing" in {
        import SendingActor._
        val props = getProps(testActor)
        val sendingActor = system.actorOf(props, "sendingActor")
        
        val size = 1000
        val maxInclusize = 1000000
        
        def randomEvents() = (0 until size).map{ _ =>
          Event(Random.nextInt(maxInclusize))
        }.toVector
        
        val unsorted = randomEvents()
        sendingActor ! DoSortEventsIncre(unsorted)
        sendingActor ! DoSortEventsDecre(unsorted)
        expectMsgPF(){
          case SortedEvents(events) =>
            events.size must be(size)
            unsorted.sortBy { _.id } must be(events)
        }
        expectMsgPF(){
          case SortedEvents(events) =>
            events.size must be(size)
            unsorted.sortBy { _.id*(1) } must be(events)
        }
        
      }
    }
}

object SendingActor {
  case class Event(id: Long)
  case class DoSortEventsIncre(unsorted: Vector[Event])
  case class DoSortEventsDecre(unsorted: Vector[Event])
  case class SortedEvents(sorted: Vector[Event])
  
  def getProps(receiver: ActorRef) = Props(new SendingActor(receiver))
}

class SendingActor(receiver: ActorRef) extends Actor {
  import SendingActor._

  def receive: Actor.Receive = {
    case DoSortEventsIncre(unsorted) =>
      receiver ! SortedEvents(unsorted.sortBy { _.id })
    case DoSortEventsDecre(unsorted)=>
      receiver ! SortedEvents(unsorted.sortWith(_.id > _.id))
      
  }
}