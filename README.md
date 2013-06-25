Akka Queues
===========

> Very much work in progress ...

Queues and Topics implemented using Akka. 

Initial design
--------------

A `Queue` is an actor that receives messages from one or more `Producers` and
then dispatches the messages to one or more (depending on configuration)
`Consumers`.

A `QueueManager` manages queues as child actors, and provides a protocol for
`Consumers` and `Producers` to connect to a queue.

A `Queue` can be configured to `broadcast` messages. This basically creates a
pub-sub topic where each message is sent to every `Consumer`. The standard
behavior is to dispatch a message to _one_ of the `Consumers` using some
round-robin strategy (if there are more than one). Another difference is that a
proper queue can be configured to hold messages until there are `Consumers`
connected to the queue.

### Protocol

    implicit val actorSystem = ActorSystem("sys")
    val queueManager = QueueManager(actorSystem)
    val protocol = QueueManager.Protocol(queueManager)
    protocol.publish("queueName", "some message")

    val consumer = actor(new Act {
            become {
                case Message(payload) => println("Received " + payload)
            }
        })
    protocol.subscribeTo("queueName", consumer)
    protocol.unsubscribeFrom("queueName", consumer)


Plans
-----

* Distributed/Remote producers/consumers (using akka remoting)
* Clustered queues (using akka cluster)
* "Reliable" messaging - implementing a producer/consumer `ack` protocol.


