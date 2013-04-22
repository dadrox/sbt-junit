package com.dadrox.sbt.junit;

import org.scalatools.testing.EventHandler;
import org.scalatools.testing.Result;

import java.util.HashSet;
import java.util.Set;

class JunitEventHandler {
    private EventHandler eventHandler;
    private JunitLogger logger;

    public JunitEventHandler(EventHandler eventHandler, JunitLogger logger) {
        this.eventHandler = eventHandler;
        this.logger = logger;
    }

    private Set<String> reported = new HashSet<String>();

    public void handle(Event event) {
        // prevent reporting on "finished" if we've already reported a failure...
        boolean alreadyReported = !(reported.add(event.testName.fullyQualified()));
        Option<Result> result = event.result;

        if (!alreadyReported && !result.isDefined()) {
            logger.testPassed(event);
            eventHandler.handle(SbtEvent.apply(event));
        }
        else if (alreadyReported && !result.isDefined()) {
            // NOP
        }
        else if (result.isDefined()) {
            eventHandler.handle(SbtEvent.apply(event));
        }
    }
}