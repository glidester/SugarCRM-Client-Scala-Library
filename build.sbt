name := "SugarCRM-Client"

version := "1.0.0"

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalacOptions ++= Seq("-deprecation", "-feature")

conflictManager := ConflictManager.strict

val apacheHttpVersion = "4.3.1"

libraryDependencies ++= List(
  ("io.argonaut" %% "argonaut" % "6.0.4")
    .exclude("org.scala-lang", "scala-library"),
  "org.apache.httpcomponents" % "httpclient" % apacheHttpVersion,
  "org.apache.httpcomponents" % "fluent-hc" % apacheHttpVersion,
  ("org.scalatest" %% "scalatest" % "2.1.7" % "test")
    .exclude("org.scala-lang", "scala-library")
)

