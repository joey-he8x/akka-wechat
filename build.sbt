import spray.revolver.RevolverPlugin.Revolver

name := """akka-wechat"""

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "spray" at "http://repo.spray.io/"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"


libraryDependencies ++= {
  val akkaVersion  = "2.3.2"
  val sprayVersion = "1.3.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
      exclude ("org.scala-lang" , "scala-library"),
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
      exclude ("org.slf4j", "slf4j-api")
      exclude ("org.scala-lang" , "scala-library"),
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.6" exclude ("org.scala-lang" , "scala-library"),
    "io.spray" %% "spray-testkit" % sprayVersion % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

Revolver.settings : Seq[sbt.Def.Setting[_]]

crossPaths := false

//conflictManager := ConflictManager.loose

net.virtualvoid.sbt.graph.Plugin.graphSettings