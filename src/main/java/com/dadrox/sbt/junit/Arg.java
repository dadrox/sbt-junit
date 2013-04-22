package com.dadrox.sbt.junit;

import java.util.List;

public interface Arg<A> {
//    List<String> names();

    A defaultArg();

    A resolve(List<String> args);
}