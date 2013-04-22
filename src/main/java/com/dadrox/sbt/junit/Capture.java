package com.dadrox.sbt.junit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

class Output {
    public String sout = "";
    public String serr = "";
    public String jout = "";
    public String jerr = "";

    public Output(String sout, String serr, String jout, String jerr) {
        this.sout = sout;
        this.serr = serr;
        this.jout = jout;
        this.jerr = jerr;
    }
}

public class Capture {
//    PrintStream javaout = System.out;
//    PrintStream javaerr = System.err;
//
//    ByteArrayOutputStream jout = new ByteArrayOutputStream();
//    ByteArrayOutputStream jerr = new ByteArrayOutputStream();

    ByteArrayOutputStream sout = new ByteArrayOutputStream();
    ByteArrayOutputStream serr = new ByteArrayOutputStream();

    PrintStream scalaout = scalaStream("out");
    PrintStream scalaerr = scalaStream("err");

    private PrintStream scalaStream(String name) {
        try {
            Class<?> cl = Class.forName("scala.Console");
            Method m = cl.getMethod(name);
            return (PrintStream) m.invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    private void setScalaStream(PrintStream stream, String name) {
        try {
            Class.forName("scala.Console").getMethod(name, PrintStream.class).invoke(null, stream);
        } catch (Exception e) {
            // ignore
        }
    }

    public Capture start() {

        if (scalaout != null) setScalaStream(new PrintStream(sout, true), "setOut");
        if (scalaerr != null) setScalaStream(new PrintStream(serr, true), "setErr");

//        System.out.flush();
//        System.setOut(new PrintStream(jout, true));

//        System.err.flush();
//        System.setErr(new PrintStream(jerr, true));
        return this;
    }

    public Output take() {
//        System.out.flush();
//        System.err.flush();
        Output result = new Output(new String(sout.toByteArray()), new String(serr.toByteArray()), /*new String(jout.toByteArray())*/"",
                        /*new String(jerr.toByteArray())*/"");
//        jerr.reset();
//        jout.reset();
//        serr.reset();
//        sout.reset();
        return result;
    }

    public void stop() {
        if (scalaout != null) setScalaStream(scalaout, "setOut");
        if (scalaerr != null) setScalaStream(scalaerr, "setErr");

//        System.out.flush();
//        System.setOut(javaout);
//        System.err.flush();
//        System.setErr(javaerr);
    }
}
