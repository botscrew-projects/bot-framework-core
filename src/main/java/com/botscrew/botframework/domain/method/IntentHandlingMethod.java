package com.botscrew.botframework.domain.method;


import com.botscrew.botframework.annotation.NlpResponse;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;

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
        if (parameter.isAnnotationPresent(NlpResponse.class)) {
            return new CompositeParameter(ArgumentType.NATIVE_NLP_RESPONSE, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.COMPLEX_TYPE);
        return new CompositeParameter(baseType, parameter);
    }
}
