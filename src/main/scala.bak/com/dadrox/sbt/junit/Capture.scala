package com.dadrox.sbt.junit

class Capture {
    import java.io.ByteArrayOutputStream
    import java.io.PrintStream

    val scalaout = scala.Console.out
    val scalaerr = scala.Console.err

    val serr = new ByteArrayOutputStream
    val sout = new ByteArrayOutputStream

    def start = {
        scala.Console.setErr(new PrintStream(serr, true))
        scala.Console.setOut(new PrintStream(sout, true))
        this
    }

    def take = {
        val result = (new String(sout.toByteArray()), new String(serr.toByteArray()))
        serr.reset()
        sout.reset()
        result
    }

    def stop = {
        scala.Console.setErr(scalaerr)
        scala.Console.setOut(scalaout)
    }
}