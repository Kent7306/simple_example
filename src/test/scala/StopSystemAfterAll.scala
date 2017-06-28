import org.scalatest.BeforeAndAfterAll
import akka.testkit.TestKit
import org.scalatest.Suite

trait StopSystemAfterAll extends BeforeAndAfterAll
{
  this: TestKit with Suite =>
  override protected def afterAll(){
    super.afterAll()
    system.shutdown()
  }
}