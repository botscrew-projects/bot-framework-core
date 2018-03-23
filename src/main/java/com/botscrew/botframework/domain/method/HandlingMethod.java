package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.composer.ArgumentsComposer;
import com.botscrew.botframework.domain.argument.composer.ArgumentsComposerFactory;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.exception.MethodSignatureException;
import com.botscrew.botframework.exception.ProcessorInnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class HandlingMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlingMethod.class);
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

    private Object instance;
    private Method method;
    private ArgumentsComposer argumentsComposer;
    private List<CompositeParameter> parameters;

    private HandlingMethod() {}

    public HandlingMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;

        buildCompositeParameters();
        checkIfMethodIsCorrect();
        argumentsComposer = ArgumentsComposerFactory.create(this.parameters);
    }

    private void checkIfMethodIsCorrect() {
        if (!Modifier.isPublic(method.getModifiers())) {
            LOGGER.warn("Method " + method.getName() + " from " + instance.getClass().toString() + " is not public.");
        }

        Set<String> names = new HashSet<>();
        Set<ArgumentType> types = new HashSet<>();

        for (CompositeParameter parameter : parameters) {
            if (parameter.hasName() && names.contains(parameter.getName())) {
                throw new MethodSignatureException("Method: " + instance.getClass().getName() + "." + method.getName()
                        + "() contains params with the same name: " + parameter.getName());
            }
            names.add(parameter.getName());

            if (!parameter.hasName() && types.contains(parameter.getType())) {
                throw new MethodSignatureException("Method: " + instance.getClass().toString() + "." + method.getName()
                        + "() contains params without names and the same type: " + parameter.getType());
            }
            types.add(parameter.getType());
        }
    }

    public void invoke(ArgumentKit kit) {
        try {
            Object[] args = argumentsComposer.compose(kit);
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProcessorInnerException("Cannot process instance method", e);
        }
    }

    private void buildCompositeParameters() {
        Parameter[] originalParameters = this.method.getParameters();
        CompositeParameter[] compositeParams = new CompositeParameter[originalParameters.length];

        for (int i = 0; i < originalParameters.length; i++) {
            compositeParams[i] = createCompositeParameter(originalParameters[i]);
        }

        this.parameters = Arrays.asList(compositeParams);
    }

    Optional<ArgumentType> getBaseTypeArgumentByClass(Class<?> type) {
        return Optional.ofNullable(supportedBaseTypes.get(type));
    }

    protected abstract CompositeParameter createCompositeParameter(Parameter parameter);
}
