package com.dadrox.sbt.junit;

abstract class Name {

    protected String suite;

    public Name(String suite) {
        this.suite = suite;
    }

    abstract String fullyQualified();

    abstract String decorated();
}