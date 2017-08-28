name := "ProvidersTest"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "3.0.0-SNAP5" % "test",
  "com.typesafe.play" % "play-json_2.11" % "2.4.2")
