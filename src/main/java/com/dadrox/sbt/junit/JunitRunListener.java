package com.dadrox.sbt.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

class JunitRunListener extends RunListener {
    SuiteName suiteName;
    JunitEventHandler eh;
    JunitLogger logger;
    Config config;

    boolean suiteIgnored = false;
    Capture capture = new Capture();// .start();

//    private static final ThreadLocal<Capture> capture = new ThreadLocal<Capture>() {
//        @Override
//        protected Capture initialValue()
//        {
//            return new Capture();
//        }
//    };

    public JunitRunListener(String testSuiteName, JunitEventHandler eh, JunitLogger logger, Config config) {
        this.suiteName = new SuiteName(testSuiteName);
        this.eh = eh;
        this.logger = logger;
        this.config = config;
        if (!config.outputEnabled) capture.start();
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        TestName name = new TestFailure(failure);
        logger.skipped("Skipped " + name.decorated() + " assumption failed -> " + failure.getMessage());
        eh.handle(new Event(name, new Some(org.scalatools.testing.Result.Skipped),
                        new Some("Assumption Failed [" + failure.getMessage() + "]"),
                        new Some(failure.getException())));
    }

    @Override
    public void testFailure(Failure failure) {
        TestFailure name = new TestFailure(failure);
        logger.testFailed(name);

        if (config.verboseTraces) logger.error(new FormattedMessage(name.fullyQualified() + " stacktrace:\n" + failure.getTrace()));
        else if (config.shortTraces) {
            String trimmedTrace = failure.getTrace().split("^*at sun.reflect")[0]; // ghetto
            logger.error(new FormattedMessage(name.fullyQualified() + " trimmed stacktrace:\n" + trimmedTrace));
        }

        eh.handle(new Event(name, new Some(org.scalatools.testing.Result.Failure), new Some(failure.getMessage()), new Some(failure.getException())));
    }

    @Override
    public void testIgnored(Description description) {
        if (description.getMethodName() == null) {
            suiteIgnored = true;
            eh.handle(new Event(suiteName, new Some(org.scalatools.testing.Result.Skipped), new Some("Ignored"), new None()));
        } else {
            TestName name = TestName.apply(description);
            logger.skipped("Ignored " + name.decorated());
            eh.handle(new Event(name, new Some(org.scalatools.testing.Result.Skipped), new Some("Ignored"), new None()));
        }
    }

    @Override
    public void testStarted(Description description) {
        logger.testStarted(TestName.apply(description));
    }

    private Option<String> testOutput() {
        if (!config.outputEnabled) {
//            capture.stop();
//            Output o = capture.take();
//            String out = "";
//            if(!o.jout.equals("")) out += o.jout;
//            if(!o.sout.equals("")) out += o.sout;
//            String err = "";
//            if(!o.jerr.equals("")) out += o.jerr;
//            if(!o.serr.equals("")) out += o.serr;
//            if (out.equals("") && err.equals("")) return new None<String>();
//            else if (err.equals("")) return new Some<String>("stdout:\n" + out);
//            else if (out.equals("")) return new Some<String>("stderr:\n" + err);
//            else return new Some<String>("stdout:\n" + out + "stderr:\n" + err);
        }
        return new None();
    }

    @Override
    public void testFinished(Description description) {
        TestName name = TestName.apply(description);
        Option<String> output = testOutput();
//        printOutput(output);
        eh.handle(new Event(name, new None(), output, new None<Throwable>()));
    }

    private void printOutput(Option<String> output) {
        if (!config.outputEnabled) {
            if (output.isDefined()) {
                String msg = output.get();
                logger.info(new FormattedMessage(msg));
            }
        }
    }

    @Override
    public void testRunStarted(Description description) {
        logger.suiteStarted(suiteName);
    }

    @Override
    public void testRunFinished(Result r) {
        if (!config.outputEnabled) capture.stop();
        JunitResult result = JunitResult.apply(r);
        boolean success = result.successful;
        if (!success) logger.suiteFailed("Failed " + suiteName.decorated() + result.description());
        else if (suiteIgnored) logger.skipped("Ignored " + suiteName.decorated() + result.description());
        else logger.suitePassed("Passed " + suiteName.decorated() + result.description());
    }
}