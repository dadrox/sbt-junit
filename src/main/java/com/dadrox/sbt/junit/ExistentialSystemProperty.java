package com.dadrox.sbt.junit;

public class ExistentialSystemProperty {
    private String property;
    private boolean defaultValue;

    public ExistentialSystemProperty(String property, boolean defaultValue) {
        this.property = property;
        this.defaultValue = defaultValue;
    }

    public boolean exists() {
        String p = System.getProperty(property);
        if (p != null) return true;
        return false;
    }

    public boolean resolve() {
        if (exists()) return true;
        return defaultValue;
    }
}
