package net.gnejs.akka.queues

import akka.actor._

// Namespace for the messaging protocol between actors.
object Protocol {

  /**
   * Sent to a queue to register the provided ref
   * as a subscriber.
   */
  case class Subscribe(subscriber: ActorRef)

  /**
   * Sent to a queue to unregister the provided ref
   * as a subscriber.
   */
  case class Unsubscribe(subscriber: ActorRef)

  /**
   * Sending an envelope to a queue will dispatch the envelope 
   * to one or more subscriber (depending on the queue configuration).
   * TODO: Add additional meta-data to the Envelope to capture ack, msg-id, reply-ref, ttl.
   */
  case class Envelope(body: Any)

}

/**
 * A Queue that dispatches incoming messages to all
 * subscribers.
 * TODO: Define parameters for queue behavior. Max queue-size etc.
 */
class Queue extends Actor {
  import Protocol._

  var subscribers = Set[ActorRef]()

  def receive = {
    case Subscribe(subscriber) => 
      println("Subscribing %s".format(subscriber))
      subscribers += subscriber
      context.watch(subscriber)
    case Unsubscribe(subscriber) =>
      println("Unsubscribing %s".format(subscriber))
      subscribers -= subscriber
      context.unwatch(subscriber)
    case Terminated(ref) =>
      println("Terminated subscriber: %s".format(ref))
      subscribers -= ref
    case e: Envelope =>
      println("Dispatching envelope %s to subscribers".format(e))
      subscribers.foreach(_ ! e)
  }
}

object Main extends App {

  import Protocol._
  implicit val system = ActorSystem("sys")

  class Consumer extends Actor {
    def receive = {
      case x => println("Consumer %s received %s".format(self.path, x))
    }
  }

  val c1 = system.actorOf(Props[Consumer])
  val c2 = system.actorOf(Props[Consumer])
  val q = system.actorOf(Props[Queue])

  q ! Subscribe(c1)
  q ! Envelope(1)
  q ! Subscribe(c2)
  q ! Envelope(2)
  q ! Envelope(3)
  q ! Unsubscribe(c1)
  q ! Envelope(4)

  Thread sleep 5000
  system.shutdown()

}
