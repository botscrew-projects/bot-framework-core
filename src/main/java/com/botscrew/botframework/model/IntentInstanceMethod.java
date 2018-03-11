package com.botscrew.botframework.model;

import com.botscrew.botframework.annotation.NlpParameters;
import com.botscrew.botframework.annotation.StateParameters;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class IntentInstanceMethod extends AbstractMethod {

    public IntentInstanceMethod(Object instance, Method method) {
        super(instance, method);
    }

    protected void buildArguments() {
        Parameter[] parameters = getMethod().getParameters();
        Argument[] arguments = new Argument[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (ChatUser.class.isAssignableFrom(parameters[i].getType())) {
                arguments[i] = new Argument(ArgumentType.USER, parameters[i]);
                continue;
            }
            if (parameters[i].isAnnotationPresent(NlpParameters.class)) {
                arguments[i] = new Argument(ArgumentType.NLP_PARAMS, parameters[i]);
                continue;
            }
            if (parameters[i].isAnnotationPresent(StateParameters.class)) {
                arguments[i] = new Argument(ArgumentType.STATE_PARAMETERS, parameters[i]);
                continue;
            }

            Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameters[i].getType());
            if (baseTypeOpt.isPresent()) {
                arguments[i] = new Argument(baseTypeOpt.get(), parameters[i]);
            }
            else {
                arguments[i] = new Argument(ArgumentType.UNKNOWN, parameters[i]);
            }
        }

        this.arguments = Arrays.asList(arguments);
    }
}
