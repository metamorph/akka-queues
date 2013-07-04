name := "net.gnejs.akka.queues"

version := "1.0"

scalaVersion := "2.10.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.0-RC1"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.2.0-RC1"

resolvers += "OSS snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"

resolvers += "OSS releases" at "http://oss.sonatype.org/content/repositories/releases"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
