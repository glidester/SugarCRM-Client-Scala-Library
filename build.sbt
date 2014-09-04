name := "SugarCRM-Client"

version := "0.5.1"

description := "Provides a simple wrapper for the sugarcrm api."

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

scalacOptions ++= Seq("-deprecation", "-feature")

javacOptions ++= Seq("-encoding", "UTF-8")

incOptions := incOptions.value.withNameHashing(true)

conflictManager := ConflictManager.strict

val apacheHttpVersion = "4.3.5"

libraryDependencies ++= List(
  ("io.argonaut" %% "argonaut" % "6.0.4")
    .exclude("org.scala-lang", "scala-library"),
  "org.apache.httpcomponents" % "httpclient" % apacheHttpVersion,
  "org.apache.httpcomponents" % "fluent-hc" % apacheHttpVersion,
  ("org.scalatest" %% "scalatest" % "2.2.2" % "test")
    .exclude("org.scala-lang.modules", "*")
)

