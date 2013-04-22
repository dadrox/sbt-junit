package com.dadrox.sbt.junit

import org.scalatools.testing
import org.scalatools.testing.{ Runner2, Logger, Fingerprint, EventHandler }
import org.junit.runner.notification.RunListener

class JunitRunner(
    classLoader: ClassLoader,
    loggers: Array[Logger])
        extends Runner2 {
    import org.junit.runner.{ JUnitCore, Request }

    def run(testSuiteName: String, fingerprint: Fingerprint, eh: EventHandler, args: Array[String]) {
        val config = new Args(args).config

        val logger = new JunitLogger(loggers, config)
        val eventHandler = new JunitEventHandler(eh, logger)
        val junit = new JUnitCore
        junit.addListener(new JunitRunListener(testSuiteName, eventHandler, logger, config))

        try {
            val testClass = classLoader.loadClass(testSuiteName)
            val result = JunitResult(junit.run(Request.aClass(testClass)))
        } catch {
            case e: Exception =>
                eventHandler.handle(Event(SuiteName(testSuiteName), Some(testing.Result.Error),
                    description = Some("Unexpected exception while running Junit tests"),
                    error = Some(e)))
        }
    }
}

class JunitEventHandler(eventHandler: EventHandler, logger: JunitLogger) {
    import scala.collection.mutable.HashSet

    val reported = new HashSet[String]

    def handle(event: Event) {
        // prevent reporting on "finished" if we've already reported a failure...
        val alreadyReported = !(reported add event.testName.fullyQualified)
        (alreadyReported, event.result) match {
            case (false, None) =>
                logger.testPassed(event)
                eventHandler.handle(SbtEvent(event))
            case (true, None)      =>
            case (_, Some(result)) => eventHandler.handle(SbtEvent(event))
        }
    }
}

class JunitRunListener(
    testSuiteName: String,
    eh: JunitEventHandler,
    logger: JunitLogger,
    config: Config)
        extends RunListener {
    import org.junit.runner.notification.Failure
    import org.junit.runner.{ Result, Description }

    var suiteIgnored = false
    val capture = new Capture().start

    private val suiteName = SuiteName(testSuiteName)

    override def testAssumptionFailure(failure: Failure) {
        val name = TestFailure(failure)
        logger.skipped("Skipped " + name.decorated + " assumption failed -> " + failure.getMessage())
        eh.handle(Event(name, Some(testing.Result.Skipped),
            description = Some("Assumption Failed [" + failure.getMessage() + "]"),
            error = Some(failure.getException())))
    }

    override def testFailure(failure: Failure) {
        val name = TestFailure(failure)
        logger.testFailed(name)

        val output = testOutput

        (config.verboseTraces, config.shortTraces) match {
            case (true, _) => logger.error(FormattedMessage(name.fullyQualified + " stacktrace:\n" + failure.getTrace()))
            case (_, true) =>
                val trimmedTrace = failure.getTrace.split("^*at sun.reflect")(0) // ghetto
                logger.error(FormattedMessage(name.fullyQualified + " trimmed stacktrace:\n" + trimmedTrace))
            case _ =>
        }

        eh.handle(Event(name, Some(testing.Result.Failure), Some(failure.getMessage()), Some(failure.getException())))
    }

    override def testIgnored(description: Description) {
        if (description.getMethodName() == null) {
            suiteIgnored = true
            eh.handle(Event(suiteName, Some(testing.Result.Skipped),
                description = Some("Ignored")))
        } else {
            val name = TestName(description)
            logger.skipped("Ignored " + name.decorated)
            eh.handle(Event(name, Some(testing.Result.Skipped),
                description = Some("Ignored")))
        }
    }

    override def testStarted(description: Description) {
        logger.testStarted(TestName(description))
    }

    private def testOutput() = capture.take match {
        case ("", "")   => None
        case (out, "")  => Some("stdout:\n" + out)
        case ("", err)  => Some("stderr:\n" + err)
        case (out, err) => Some("stdout:\n" + out + "stderr:\n" + err)
    }

    override def testFinished(description: Description) {
        val name = TestName(description)
        val output = testOutput
        if (config.outputEnabled) {
            output.map(o => logger.info(FormattedMessage.apply(o)))
        }
        eh.handle(Event(name, result = None, description = output))
    }

    override def testRunStarted(description: Description) {
        logger.suiteStarted(suiteName)
    }

    override def testRunFinished(r: Result) {
        capture.stop
        val result = JunitResult(r)
        (result.successful, suiteIgnored) match {
            case (false, _) => logger.suiteFailed("Failed " + suiteName.decorated + result.description)
            case (_, true)  => logger.skipped("Ignored " + suiteName.decorated + result.description)
            case _          => logger.suitePassed("Passed " + suiteName.decorated + result.description)
        }
    }
}