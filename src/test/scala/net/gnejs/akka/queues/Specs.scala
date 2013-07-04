package net.gnejs.akka.queues

import org.scalatest.FunSpec
import akka.actor._
import akka.testkit._
import Protocol._

class ExampleSpec extends FunSpec {

  describe("A Queue configured as topic") {
    it("should dispatch any envelope to all subscribers") {
      implicit lazy val sys = ActorSystem("sys")
      val queue = sys.actorOf(Props[Queue])
      val subscriber = TestProbe()
      queue ! Subscribe(subscriber.ref)
      queue ! Envelope("foo")
      assert(subscriber.expectMsgClass(classOf[Envelope]).body == "foo")
      sys.shutdown()
    }
  }

}
