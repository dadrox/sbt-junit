package com.dadrox.sbt.junit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.scalatools.testing.EventHandler;
import org.scalatools.testing.Fingerprint;
import org.scalatools.testing.Logger;
import org.scalatools.testing.Result;
import org.scalatools.testing.Runner2;

class JunitRunner extends Runner2 {

    private ClassLoader classLoader;
    private Logger[] loggers;

    public JunitRunner(ClassLoader classLoader, Logger[] loggers) {
        this.classLoader = classLoader;
        this.loggers = loggers;
    }

    @Override
    public void run(String testSuiteName, Fingerprint fingerprint, EventHandler eh, String[] args) {
        Config config = new Args(args).config();

        JunitLogger logger = new JunitLogger(loggers, config);
        JunitEventHandler eventHandler = new JunitEventHandler(eh, logger);
        JUnitCore junit = new JUnitCore();
        junit.addListener(new JunitRunListener(testSuiteName, eventHandler, logger, config));

        try {
            Class<?> testClass = classLoader.loadClass(testSuiteName);
            JunitResult.apply(junit.run(Request.aClass(testClass)));
        } catch (Exception e) {
            eventHandler.handle(new Event(new SuiteName(testSuiteName), new Some(Result.Error),
                            new Some("Unexpected exception while running Junit tests"), new Some(e)));
        }
    }
}