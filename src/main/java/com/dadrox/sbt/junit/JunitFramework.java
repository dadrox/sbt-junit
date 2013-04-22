package com.dadrox.sbt.junit;

import org.scalatools.testing.AnnotatedFingerprint;
import org.scalatools.testing.Fingerprint;
import org.scalatools.testing.Logger;
import org.scalatools.testing.Runner;
import org.scalatools.testing.Framework;
import org.scalatools.testing.SubclassFingerprint;

public class JunitFramework implements Framework {
    private static SubclassFingerprint Junit3Fingerprint = new SubclassFingerprint() {
        @Override
        public boolean isModule() {
            return false;
        }

        @Override
        public String superClassName() {
            return "junit.framework.TestCase";
        }
    };

    private static AnnotatedFingerprint Junit4Fingerprint = new AnnotatedFingerprint() {
        @Override
        public String annotationName() {
            return "org.junit.Test";
        }

        @Override
        public boolean isModule() {
            return false;
        }
    };

    @Override
    public String name() {
        System.out.println("Someone get name!");

        return "Junit";
    }

    @Override
    public Runner testRunner(ClassLoader classLoader, Logger[] loggers) {
        return new JunitRunner(classLoader, loggers);
    }

    @Override
    public Fingerprint[] tests() {
        return new Fingerprint[] { Junit3Fingerprint, Junit4Fingerprint };
    }
}