package com.botscrew.botframework.model;

import com.botscrew.botframework.annotation.Param;

import java.lang.reflect.Parameter;

public class Argument {
    private final ArgumentType type;
    private final Parameter parameter;

    public Argument(ArgumentType type, Parameter parameter) {
        this.type = type;
        this.parameter = parameter;
    }

    public String getName() {
        if (parameter.isNamePresent()) return parameter.getName();

        if (parameter.isAnnotationPresent(Param.class)) {
            return parameter.getAnnotation(Param.class).value();
        }
        return parameter.getName();
    }

    public ArgumentType getType() {
        return type;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
