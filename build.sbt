name := "sbt-junit"

organization := "com.dadrox"

version := "0.3"

libraryDependencies += "junit" % "junit-dep" % "4.10"

libraryDependencies += "org.scala-tools.testing" % "test-interface" % "0.5"

crossPaths := false

autoScalaLibrary := false

javacOptions in compile ++= List("-target", "1.5", "-source", "1.5")
