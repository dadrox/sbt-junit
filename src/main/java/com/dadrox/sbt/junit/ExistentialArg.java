package com.dadrox.sbt.junit;

import java.util.List;

public class ExistentialArg implements Arg<Boolean> {
    private List<String> _names;
    private boolean _defaultArg;

    public ExistentialArg(List<String> names, boolean defaultArg) {
        this._names = names;
        this._defaultArg = defaultArg;
    }

    @Override
    public Boolean defaultArg() {
        return _defaultArg;
    }

    @Override
    public Boolean resolve(List<String> args) {
        for (String arg : args) {
            for (String name : _names) {
                if (name.equals(arg)) return true;
            }
        }
        return _defaultArg;
    }
}
