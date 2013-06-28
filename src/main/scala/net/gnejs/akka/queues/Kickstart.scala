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
      subscribers += subscriber
      context.watch(subscriber)
    case Unsubscribe(subscriber) =>
      subscribers -= subscriber
      context.unwatch(subscriber)
    case Terminated(ref) =>
      subscribers -= ref
    case e: Envelope =>
      subscribers.foreach(_ ! e)
  }
}

object Main extends App {
  // TODO: Write simple test
}
