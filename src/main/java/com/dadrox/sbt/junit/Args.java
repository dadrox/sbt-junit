package com.dadrox.sbt.junit;

import java.util.Arrays;
import java.util.List;

public class Args {
    private String[] argsArray = new String[0];

    public Args(String[] argsArray) {
        this.argsArray = argsArray;
    }

    private List<String> args() {
        return Arrays.asList(argsArray);
    }

    private List<String> list(String... args) {
        return Arrays.asList(args);
    }

    private ValueSystemProperty sbtLogFormattingDisabled = new ValueSystemProperty("sbt.log.noformat", "true", false);

    private boolean noColor() {
        return new ExistentialArg(list("--no-color", "-nc"), false).resolve(args()) || sbtLogFormattingDisabled.resolve();
    }

    private boolean enableOutput() {
        return new ExistentialArg(list("--verbose-output", "-vo"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.verbose.output", false).resolve();
    }

    private boolean enableVerboseTraces() {
        return new ExistentialArg(list("--verbose-traces", "-tv"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.traces.verbose", false).resolve();
    }

    private boolean enableShortTraces() {
        return new ExistentialArg(list("--short-traces", "-ts"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.traces.short", false).resolve();
    }

    private boolean enableTestStart() {
        return new ExistentialArg(list("--verbose-test-start", "-vts"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.verbose.test.start", false).resolve();
    }

    private boolean enableTestPass() {
        return new ExistentialArg(list("--verbose-test-pass", "-vtp"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.verbose.test.pass", false).resolve();
    }

    private boolean enableSuiteStart() {
        return new ExistentialArg(list("--verbose-suite-start", "-vss"), false).resolve(args()) ||
                        new ExistentialSystemProperty("sbt.reports.verbose.suite.start", false).resolve();
    }

    public Config config() {
        return new Config(
                        !noColor(),
                        enableOutput(),
                        enableVerboseTraces(),
                        enableShortTraces(),
                        enableTestStart(),
                        enableTestPass(),
                        enableSuiteStart());
    }

    private boolean argExists(String arg) {
        for (String a : args()) {
            if (a.equals(arg)) return true;
        }
        return false;
    }

    private boolean systemPropertyExists(String property) {
        String p = System.getProperty(property);
        if (p != null) return true;
        return false;

    }

    private boolean systemPropertyConfig(String property, String value) {
        String p = System.getProperty(property);
        if (p != null) {
            if (p.equals(property)) return true;
            return false;
        }
        return false;
    }
}
