name := "sbt-junit"

organization := "com.dadrox"

version := "0.4-SNAPSHOT"

libraryDependencies += "junit" % "junit-dep" % "4.10"

libraryDependencies += "org.scala-sbt" % "test-interface" % "1.0"

crossPaths := false

autoScalaLibrary := false

javacOptions in compile ++= List("-target", "1.5", "-source", "1.5")
