package com.botscrew.botframework.model;

import com.botscrew.botframework.annotation.Param;

import java.lang.reflect.Parameter;

public class CompositeParameter {
    private final ArgumentType type;
    private final Parameter originalParameter;
    private final boolean hasName;
    private final String name;

    public CompositeParameter(ArgumentType type, Parameter parameter) {
        this.type = type;
        this.originalParameter = parameter;
        this.hasName = detectIfNameIsPresent(parameter);
        this.name = detectName(parameter);
    }

    public Class getOriginalType() {
        return this.originalParameter.getType();
    }

    public boolean hasName() {
        return hasName;
    }

    public String getName() {
        return name;
    }

    private static boolean detectIfNameIsPresent(Parameter parameter) {
        return parameter.isAnnotationPresent(Param.class);
    }

    private static String detectName(Parameter parameter) {
        if (parameter.isAnnotationPresent(Param.class)) {
            return parameter.getAnnotation(Param.class).value();
        }
        return parameter.getName();
    }

    public ArgumentType getType() {
        return type;
    }

    public Parameter getOriginalParameter() {
        return originalParameter;
    }
}
