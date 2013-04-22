package com.dadrox.sbt.junit;

import org.scalatools.testing.Result;

abstract class Option<A> {
    public static <B> Option<B> apply(B value) {
        if (value == null) return new None<B>();
        return new Some<B>(value);
    }

    abstract A get();

    abstract A getOrElse(A defaultValue);

    abstract boolean isDefined();
}

class None<A> extends Option<A> {
    @Override
    A getOrElse(A defaultValue) {
        return defaultValue;
    }

    @Override
    boolean isDefined() {
        return false;
    }

    @Override
    A get() {
        return null;
    }
}

class Some<A> extends Option<A> {
    private A value;

    public Some(A value) {
        this.value = value;
    }

    @Override
    A getOrElse(@SuppressWarnings("unused") A defaultValue) {
        return value;
    }

    @Override
    boolean isDefined() {
        return true;
    }

    @Override
    A get() {
        return value;
    }
}

class Event {
    public Name testName;
    public Option<Result> result;
    public Option<String> description; // = none
    public Option<Throwable> error; // = None

    public Event(Name testName, Option<Result> result, Option<String> description, Option<Throwable> error) {
        this.testName = testName;
        this.result = result;
        this.description = description;
        this.error = error;
    }
}

class SbtEvent implements org.scalatools.testing.Event {

    public String testName;
    public Result result;
    public String description = "";
    public Throwable error = null;

    public SbtEvent(String testName, Result result, String description, Throwable error) {
        this.testName = testName;
        this.result = result;
        this.description = description;
        this.error = error;
    }

    public static SbtEvent apply(Event event) {
        return new SbtEvent(
                        event.testName.fullyQualified(),
                        event.result.getOrElse(Result.Success),
                        event.description.getOrElse(""),
                        event.error.getOrElse(null));
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public Throwable error() {
        return error;
    }

    @Override
    public Result result() {
        return result;
    }

    @Override
    public String testName() {
        return testName;
    }
}
