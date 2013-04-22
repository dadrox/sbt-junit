package com.dadrox.sbt.junit;

import org.junit.runner.notification.Failure;
import java.util.List;

class JunitResult {
    public boolean successful;
    public int runCount;
    public long runTime;
    public int ignoreCount;
    public int failureCount;
    public List<Failure> failures;

    public static JunitResult apply(org.junit.runner.Result result) {
        return new JunitResult(
                        result.wasSuccessful(), result.getRunCount(), result.getRunTime(), result.getIgnoreCount(), result.getFailureCount(),
                        result.getFailures());
    }

    public JunitResult(boolean successful, int runCount, long runTime, int ignoreCount, int failureCount, List<Failure> failures) {
        this.successful = successful;
        this.runCount = runCount;
        this.runTime = runTime;
        this.ignoreCount = ignoreCount;
        this.failureCount = failureCount;
        this.failures = failures;
    }

    public String description() {
        return ", Total " + runCount + ", Failed " + failureCount + ", Ignored " + ignoreCount + " in " + runTime + " ms";
    }
}