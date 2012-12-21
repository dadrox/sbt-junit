name := "sbt-junit"

organization := "com.dadrox"

version := "0.1"

libraryDependencies += "junit" % "junit-dep" % "4.10"

libraryDependencies += "org.scala-tools.testing" % "test-interface" % "0.5"

crossScalaVersions := Seq("2.9.1", "2.9.3-RC1", "2.10.0-RC5", "2.9.2")

scalaVersion := "2.9.2"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }
