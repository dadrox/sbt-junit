package com.dadrox.sbt.junit;

public class ValueSystemProperty {
    private String property;
    private String enableValue;
    private boolean defaultValue;

    public ValueSystemProperty(String property, String enableValue, boolean defaultValue) {
        this.property = property;
        this.enableValue = enableValue;
        this.defaultValue = defaultValue;
    }

    public boolean exists() {
        String p = System.getProperty(property);
        if (p != null) return true;
        return false;
    }

    public boolean resolve() {
        String p = System.getProperty(property);
        if (p != null) {
            if (p.equals(enableValue)) return true;
            return false;
        }
        return defaultValue;
    }
}

//case class ValueSystemProperty(property: String, enableValue: String, default: Boolean) {
//    def exists() = Option(System.getProperty(property)).isDefined
//    def resolve() = Option(System.getProperty(property)) match {
//        case Some(`enableValue`) => true
//        case Some(otherValue)    => false
//        case _                   => default
//    }
//}