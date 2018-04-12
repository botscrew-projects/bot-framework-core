package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.annotation.Original;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.user.ChatUser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class StateWithOriginalHandlingMethod extends HandlingMethod {
    public StateWithOriginalHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }

        if (parameter.isAnnotationPresent(Original.class)) {
            return new CompositeParameter(ArgumentType.ORIGINAL_RESPONSE, parameter);
        }

        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());
        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.UNKNOWN);
        return new CompositeParameter(baseType, parameter);
    }
}
