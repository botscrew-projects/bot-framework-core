package com.botscrew.botframework.domain.method;


import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;
import com.botscrew.botframework.model.CompositeParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class IntentHandlingMethod extends HandlingMethod {

    public IntentHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }
        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.UNKNOWN);
        return new CompositeParameter(baseType, parameter);
    }
}
