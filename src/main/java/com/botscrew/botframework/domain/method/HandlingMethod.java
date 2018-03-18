package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.ArgumentsComposer;
import com.botscrew.botframework.domain.ArgumentsComposerFactory;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.CompositeParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class HandlingMethod {

    private Object instance;
    private Method method;
    private ArgumentsComposer argumentsComposer;
    private List<CompositeParameter> compositeParameters;

    private static final Map<Class, ArgumentType> supportedBaseTypes;

    static {
        supportedBaseTypes = new HashMap<>();

        supportedBaseTypes.put(Integer.class, ArgumentType.PARAM_INTEGER);
        supportedBaseTypes.put(Long.class, ArgumentType.PARAM_LONG);
        supportedBaseTypes.put(Byte.class, ArgumentType.PARAM_BYTE);
        supportedBaseTypes.put(Short.class, ArgumentType.PARAM_SHORT);
        supportedBaseTypes.put(Double.class, ArgumentType.PARAM_DOUBLE);
        supportedBaseTypes.put(Float.class, ArgumentType.PARAM_FLOAT);
        supportedBaseTypes.put(String.class, ArgumentType.PARAM_STRING);
    }

    private HandlingMethod() {}

    public HandlingMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;

        buildCompositeParameters();
        argumentsComposer = ArgumentsComposerFactory.create(this.getCompositeParameters());
    }

    public Object[] composeArgs(ArgumentKit kit) {
        return argumentsComposer.compose(kit);
    }

    private void buildCompositeParameters() {
        Parameter[] parameters = getMethod().getParameters();
        CompositeParameter[] compositeParams = new CompositeParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            compositeParams[i] = createCompositeParameter(parameters[i]);
        }

        this.compositeParameters = Arrays.asList(compositeParams);
    }

    protected abstract CompositeParameter createCompositeParameter(Parameter parameter);

    Optional<ArgumentType> getBaseTypeArgumentByClass(Class<?> type) {
        return Optional.ofNullable(supportedBaseTypes.get(type));
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public List<CompositeParameter> getCompositeParameters() {
        return compositeParameters;
    }
}
