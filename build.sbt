name := "SugarCRM-Client"

version := "1.0.0"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-deprecation", "-feature")

conflictManager := ConflictManager.strict

val apacheHttpVersion = "4.3.1"

libraryDependencies ++= List(
  ("io.argonaut" %% "argonaut" % "6.0.2")
    .exclude("org.scala-lang", "scala-library"),
  "org.apache.httpcomponents" % "httpclient" % apacheHttpVersion,
  "org.apache.httpcomponents" % "fluent-hc" % apacheHttpVersion,
  ("org.scalatest" % "scalatest_2.10" % "2.0" % "test")
    .exclude("org.scala-lang", "scala-library")
)

