import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import org.scalatest.MustMatchers
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorSystem
import scala.collection.mutable.ListBuffer
  

class FilteringActorTest2 extends TestKit(ActorSystem("testSystem")) 
with WordSpecLike 
with MustMatchers 
with StopSystemAfterAll{
  
}


/* "A Filtering Actor" must {
    "filter out particular messages" in {
      import FilteringActor._
      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "filter-2")
      filter ! Event(1)
      filter ! Event(2)
      filter ! Event(1)
      filter ! Event(3)
      filter ! Event(1)
      filter ! Event(4)
      filter ! Event(5)
      filter ! Event(5)
      filter ! Event(6)
      val eventsIds = receiveWhile(){
        case Event(id) if id <= 5 => id
      }
      eventsIds must be (List(1, 2, 3, 4, 5))
    }
  }*/

/*object FilteringActor {
  def props(next: ActorRef, bufferSize: Int) = {
    Props(new FilteringActor(next, bufferSize))
  }
  case class Event(id: Int)
}

class FilteringActor(next : ActorRef, bufferSize: Int) extends Actor {
  import FilteringActor._
  var messageList = ListBuffer[Event]()
  def receive: Actor.Receive = {
    case msg: Event =>
      if(!messageList.contains(msg)){
        messageList += msg
        next ! msg
        if(messageList.size > bufferSize){
          messageList.drop(1)
        }
        
      }
  }
}*/