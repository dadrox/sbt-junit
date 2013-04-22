package com.dadrox.sbt.junit

import org.junit.runner.Description
import org.junit.runner.notification.Failure
import org.scalatools.testing
import scala.collection.JavaConverters._

object JunitResult {
    def apply(result: org.junit.runner.Result) =
        new JunitResult(result.wasSuccessful(), result.getRunCount(), result.getRunTime(), result.getIgnoreCount(), result.getFailureCount(), result.getFailures().asScala.toList)
}
case class JunitResult(
        successful: Boolean,
        runCount: Int,
        runTime: Long,
        ignoreCount: Int,
        failureCount: Int,
        failures: List[Failure]) {
    val description = ", Total " + runCount + ", Failed " + failureCount + ", Ignored " + ignoreCount + " in " + runTime + " ms"
}

trait Name {
    def suite(): String
    def fullyQualified(): String
    def decorated(): String
    override lazy val toString = decorated
}
object TestName {
    def apply(description: Description): TestName = new TestResult(description.getClassName(), description.getMethodName())
    //    def apply(failure: Failure): TestName = apply(failure.getDescription())
}
trait TestName extends Name {
    def test: String
    lazy val fullyQualified = suite + "." + test
    lazy val decorated = "Test " + fullyQualified
}
case class TestFailure(failure: Failure) extends TestName {
    val suite = failure.getDescription().getClassName()
    val test = failure.getDescription().getMethodName()
}
case class TestResult(suite: String, test: String) extends TestName

object SuiteName {
    def apply(description: Description): SuiteName = new SuiteName(description.getClassName())
    def apply(failure: Failure): SuiteName = apply(failure.getDescription())
}
case class SuiteName(suite: String) extends Name {
    val fullyQualified = suite
    val decorated = "Test Suite " + suite
}

case class Event(
    testName: Name,
    result: Option[testing.Result],
    description: Option[String] = None,
    error: Option[Throwable] = None)

object SbtEvent {
    def apply(event: Event) = new SbtEvent(
        event.testName.fullyQualified,
        event.result.getOrElse(testing.Result.Success),
        event.description.getOrElse(""),
        event.error.getOrElse(null))
}
case class SbtEvent(
    testName: String,
    result: testing.Result,
    description: String = "",
    error: Throwable = null)
        extends testing.Event