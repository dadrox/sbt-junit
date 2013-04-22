package com.dadrox.sbt.junit

case class Config(
    colorsEnabled: Boolean,
    outputEnabled: Boolean,
    verboseTraces: Boolean,
    shortTraces: Boolean,
    testStart: Boolean,
    testPass: Boolean,
    suiteStart: Boolean)

trait Arg[A] {
    def names: List[String]
    def default: A
    def resolve(args: List[String]): A
}
case class ExistentialArg(names: List[String], default: Boolean) extends Arg[Boolean] {
    def resolve(args: List[String]) = args.find(names.contains) match {
        case Some(_) => true
        case _       => default
    }
}
case class ExistentialSystemProperty(property: String, default: Boolean) {
    def exists() = Option(System.getProperty(property)).isDefined
    def resolve() = exists match {
        case true => true
        case _    => default
    }
}
case class ValueSystemProperty(property: String, enableValue: String, default: Boolean) {
    def exists() = Option(System.getProperty(property)).isDefined
    def resolve() = Option(System.getProperty(property)) match {
        case Some(`enableValue`) => true
        case Some(otherValue)    => false
        case _                   => default
    }
}

class Args(argsArray: Array[String]) {
    val args = argsArray.toList

    private lazy val sbtLogFormattingDisabled = ValueSystemProperty("sbt.log.noformat", "true", false)
    private lazy val noColor = ExistentialArg(List("--no-color", "-nc"), false).resolve(args) || sbtLogFormattingDisabled.resolve

    private lazy val enableOutput = ExistentialArg(List("--verbose-output", "-vo"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.verbose.output", false).resolve

    private lazy val enableVerboseTraces = ExistentialArg(List("--verbose-traces", "-tv"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.traces.verbose", false).resolve

    private lazy val enableShortTraces = ExistentialArg(List("--short-traces", "-ts"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.traces.short", false).resolve

    private lazy val enableTestStart = ExistentialArg(List("--verbose-test-start", "-vts"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.verbose.test.start", false).resolve

    private lazy val enableTestPass = ExistentialArg(List("--verbose-test-pass", "-vtp"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.verbose.test.pass", false).resolve

    private lazy val enableSuiteStart = ExistentialArg(List("--verbose-suite-start", "-vss"), false).resolve(args) ||
    	ExistentialSystemProperty("sbt.reports.verbose.suite.start", false).resolve

    lazy val config = Config(
        colorsEnabled = !noColor,
        outputEnabled = enableOutput,
        verboseTraces = enableVerboseTraces,
        shortTraces = enableShortTraces,
        testStart = enableTestStart,
        testPass = enableTestPass,
        suiteStart = enableSuiteStart)

    private def argExists(arg: String) = args.contains(arg)
    private def systemPropertyExists(property: String) = Option(System.getProperty(property)).isDefined
    private def systemPropertyConfig(property: String, value: String) = Option(System.getProperty(property)) match {
        case Some(`value`) => true
        case _             => false
    }
}