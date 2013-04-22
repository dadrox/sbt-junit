package com.dadrox.sbt.junit

import org.scalatools.testing.Logger
import org.scalatools.testing.Runner
import org.scalatools.testing
import scala.collection.mutable.ListBuffer
import java.util.concurrent.atomic.AtomicInteger

// TODO?
// add:
// test-only filtering
//

class JunitFramework extends testing.Framework {
    override val name = "Junit"
    override val tests = Array[testing.Fingerprint](Junit3Fingerprint, Junit4Fingerprint)

    override def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new JunitRunner(classLoader, loggers)
}

object Junit3Fingerprint extends testing.SubclassFingerprint {
    override val superClassName = "junit.framework.TestCase"
    override val isModule = false
}

object Junit4Fingerprint extends testing.AnnotatedFingerprint {
    override val annotationName = "org.junit.Test"
    override val isModule = false
}