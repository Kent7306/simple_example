import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorLogging
import akka.actor.ActorRef

object Example extends App{
    class FirstActor extends Actor with ActorLogging{
        var child:ActorRef = _
        def receive = {
            case x => child ! x;
                      log.info("received" + x)  
        }
        override def preStart():Unit = {
            child = context.actorOf(Props[MyActor],name="myChild")
            log.info("preStart() in FirstActor.")
        }
        override def postStop():Unit = {
            log.info("postStop() in FirstActor")
        }
    }
    class MyActor extends Actor with ActorLogging{
        def receive = {
            case "test" => log.info("receive test!")
            case _ => log.info("received unkown message")            
        }
        
        override def preStart():Unit = {
            log.info("preStart() in MyActor.")
        }
        override def postStop():Unit = {
            log.info("postStop() in MyActor")
        }
    }
    
    val system = ActorSystem("MyActorSystem")
    val systemLog = system.log
    
    val firstActor = system.actorOf(Props[FirstActor],name="firstActor")
    systemLog.info("准备向myactor发送消息")
    
    firstActor ! "test"
    firstActor ! "123"
    Thread.sleep(5000)
    //system.
}