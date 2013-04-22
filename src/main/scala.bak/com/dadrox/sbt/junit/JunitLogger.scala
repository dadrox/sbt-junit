package com.dadrox.sbt.junit

import org.scalatools.testing.Logger

case class Color(value: String) {
    override val toString = value
}

object Colors {
    val Normal = Color("\u001b[m")
    val Green = Color("\u001b[32m")
    val Red = Color("\u001b[31m")
    val Yellow = Color("\u001b[33m")
    val Cyan = Color("\u001b[36m")
    val Grey = Color("\u001b[37m")
    val Magenta = Color("\u001b[35m")

    object Bright {
        val Green = Color("\u001b[1;32m")
        val Cyan = Color("\u001b[1;36m")
    }
}

case class FormattedMessage(msg: String)

class JunitLogger(
        loggers: Array[Logger],
        config: Config) {
    import Colors._

    val colorsEnabled = config.colorsEnabled

    //    def ansiCodesSupported(): Boolean = true

    def error(msg: => FormattedMessage) = loggers.foreach(_.error(msg.msg))
    def warn(msg: => FormattedMessage) = loggers.foreach(_.warn(msg.msg))
    def info(msg: => FormattedMessage) = loggers.foreach(_.info(msg.msg))
    def debug(msg: => FormattedMessage) = loggers.foreach(_.debug(msg.msg))
    def trace(t: => Throwable) = loggers.foreach(_.trace(t))

    def format(c: Color, msg: String) = FormattedMessage(colorsEnabled match {
        case true  => c + msg + Normal
        case false => msg.replaceAll("\\u001b.*m", "") // this may not work?
    })

    def skipped(msg: String) = warn(format(Yellow, msg))

    def testStarted(name: TestName) = {
        val msg = format(Cyan, "Started " + name.decorated)
        config.testStart match {
            case true  => info(msg)
            case false => debug(msg)
        }
    }
    def testPassed(event: Event) = {
        val msg = format(Green, "Passed " + event.testName.decorated)
        config.testPass match {
            case true  => info(msg)
            case false => debug(msg)
        }
    }
    def testFailed(name: TestFailure) = error(format(Magenta, "Failed " + name.decorated + ": " + name.failure.getMessage()))

    def suiteStarted(name: SuiteName) = {
        val msg = format(Bright.Cyan, "Started " + name.decorated)
        config.suiteStart match {
            case true  => info(msg)
            case false => debug(msg)
        }
    }
    def suitePassed(msg: String) = info(format(Green, msg))
    def suiteFailed(msg: String) = error(format(Red, msg))

}