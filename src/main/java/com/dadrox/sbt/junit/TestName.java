package com.dadrox.sbt.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

abstract class TestName extends Name {
    private String test;

    public static TestName apply(Description description) {
        return new TestResult(description.getClassName(), description.getMethodName());
    }

    public TestName(String suite, String test) {
        super(suite);
        this.test = test;
    }

    @Override
    public String fullyQualified() {
        return suite + "." + test;
    }

    @Override
    public String decorated() {
        return "Test " + fullyQualified();
    }
}

class TestFailure extends TestName {
    public Failure failure;

    public TestFailure(Failure failure) {
        super(failure.getDescription().getClassName(), failure.getDescription().getMethodName());
        this.failure = failure;
    }
}

class TestResult extends TestName {
    public TestResult(String suite, String test) {
        super(suite, test);
    }
}

class SuiteName extends Name {
    public static SuiteName apply(Description description) {
        return new SuiteName(description.getClassName());
    }

    public static SuiteName apply(Failure failure) {
        return SuiteName.apply(failure.getDescription());
    }

    public SuiteName(String suite) {
        super(suite);
    }

    @Override
    public String fullyQualified() {
        return suite;
    }

    @Override
    public String decorated() {
        return "Test Suite " + suite;
    }
}