package com.dadrox.sbt.junit;

public class Config {
    public boolean colorsEnabled;
    public boolean outputEnabled;
    public boolean verboseTraces;
    public boolean disableTraces;
    public boolean testStart;
    public boolean testPass;
    public boolean suiteStart;

    public Config(boolean colorsEnabled, boolean outputEnabled, boolean verboseTraces, boolean disableTraces, boolean testStart, boolean testPass,
                    boolean suiteStart) {
        this.colorsEnabled = colorsEnabled;
        this.outputEnabled = outputEnabled;
        this.verboseTraces = verboseTraces;
        this.disableTraces = disableTraces;
        this.testStart = testStart;
        this.testPass = testPass;
        this.suiteStart = suiteStart;
    }

    @Override
    public String toString() {
        return "Config [colorsEnabled=" + colorsEnabled + ", outputEnabled=" + outputEnabled + ", verboseTraces=" + verboseTraces + ", disableTraces="
                        + disableTraces + ", testStart=" + testStart + ", testPass=" + testPass + ", suiteStart=" + suiteStart + "]";
    }


}