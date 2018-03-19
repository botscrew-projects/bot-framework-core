package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.annotation.Text;
import com.botscrew.botframework.domain.ChatUser;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class TextHandlingMethod extends HandlingMethod {

    public TextHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }

        if (parameter.isAnnotationPresent(Text.class)) {
            return new CompositeParameter(ArgumentType.TEXT, parameter);
        }

        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.UNKNOWN);
        return new CompositeParameter(baseType, parameter);
    }
}
