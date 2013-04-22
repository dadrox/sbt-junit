package com.dadrox.sbt.junit;

import org.scalatools.testing.Logger;

public class JunitLogger {
    private Logger[] loggers;
    private Config config;

    public JunitLogger(Logger[] loggers, Config config) {
        this.loggers = loggers;
        this.config = config;
    }

    public void error(FormattedMessage msg) {
        for (Logger logger : loggers) {
            logger.error(msg.msg());
        }
    }

    public void warn(FormattedMessage msg) {
        for (Logger logger : loggers) {
            logger.warn(msg.msg());
        }
    }

    public void info(FormattedMessage msg) {
        for (Logger logger : loggers) {
            logger.info(msg.msg());
        }
    }

    public void debug(FormattedMessage msg) {
        for (Logger logger : loggers) {
            logger.debug(msg.msg());
        }
    }

    public void trace(Throwable t) {
        for (Logger logger : loggers) {
            logger.trace(t);
        }
    }

    public FormattedMessage format(Color c, String msg) {
        if (config.colorsEnabled) return new FormattedMessage(c + msg + Colors.Normal);
        return new FormattedMessage(msg.replaceAll("\\u001b.*m", "")); // this may not work?
    }

    public void skipped(String msg) {
        warn(format(Colors.Yellow, msg));
    }

    public void testStarted(TestName name) {
        FormattedMessage msg = format(Colors.Cyan, "Started " + name.decorated());
        if (config.testStart) info(msg);
        else debug(msg);
    }

    public void testPassed(Event event) {
        FormattedMessage msg = format(Colors.Green, "Started " + event.testName.decorated());
        if (config.testStart) info(msg);
        else debug(msg);
    }

    public void testFailed(TestFailure name) {
        error(format(Colors.Magenta, "Failed " + name.decorated() + ": " + name.failure.getMessage()));
    }

    public void suiteStarted(SuiteName name) {
        FormattedMessage msg = format(Colors.Bright.Cyan, "Started " + name.decorated());
        if (config.suiteStart) info(msg);
        else debug(msg);
    }

    public void suitePassed(String msg) {
        info(format(Colors.Green, msg));
    }

    public void suiteFailed(String msg) {
        error(format(Colors.Red, msg));
    }

    public static class Color {
        private String value;

        public Color(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static class Colors {
        public static Color Normal = new Color("\u001b[m");
        public static Color Green = new Color("\u001b[32m");
        public static Color Red = new Color("\u001b[31m");
        public static Color Yellow = new Color("\u001b[33m");
        public static Color Cyan = new Color("\u001b[36m");
        public static Color Grey = new Color("\u001b[37m");
        public static Color Magenta = new Color("\u001b[35m");

        public static class Bright {
            public static Color Green = new Color("\u001b[1;32m");
            public static Color Cyan = new Color("\u001b[1;36m");
        }
    }
}

class FormattedMessage {
    private String msg;

    public FormattedMessage(String msg) {
        this.msg = msg;
    }

    public String msg() {
        return msg;
    }
}