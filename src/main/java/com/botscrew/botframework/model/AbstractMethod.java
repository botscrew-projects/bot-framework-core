package com.botscrew.botframework.model;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.ArgumentsComposer;
import com.botscrew.botframework.domain.ArgumentsComposerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMethod {

    private Object instance;
    private Method method;
    private ArgumentsComposer argumentsComposer;
    protected List<CompositeParameter> compositeParameters;

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

    private AbstractMethod() {}

    public AbstractMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;

        buildCompositeParameters();
        argumentsComposer = ArgumentsComposerFactory.create(getCompositeParameters());
    }

    public Object[] composeArgs(ArgumentKit kit) {
        return argumentsComposer.compose(kit);
    }

    Optional<ArgumentType> getBaseTypeArgumentByClass(Class<?> type) {
        return Optional.ofNullable(supportedBaseTypes.get(type));
    }

    protected abstract void buildCompositeParameters();

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
