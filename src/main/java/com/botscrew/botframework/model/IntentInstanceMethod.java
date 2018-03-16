package com.botscrew.botframework.model;


import com.botscrew.botframework.annotation.StateParameters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class IntentInstanceMethod extends AbstractMethod {

    public IntentInstanceMethod(Object instance, Method method) {
        super(instance, method);
    }

    protected void buildCompositeParameters() {
        Parameter[] parameters = getMethod().getParameters();
        CompositeParameter[] compositeParameters = new CompositeParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (ChatUser.class.isAssignableFrom(parameters[i].getType())) {
                compositeParameters[i] = new CompositeParameter(ArgumentType.USER, parameters[i]);
                continue;
            }
            if (parameters[i].isAnnotationPresent(StateParameters.class)) {
                compositeParameters[i] = new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameters[i]);
                continue;
            }

            Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameters[i].getType());
            if (baseTypeOpt.isPresent()) {
                compositeParameters[i] = new CompositeParameter(baseTypeOpt.get(), parameters[i]);
            }
            else {
                compositeParameters[i] = new CompositeParameter(ArgumentType.UNKNOWN, parameters[i]);
            }
        }

        this.compositeParameters = Arrays.asList(compositeParameters);
    }
}
