package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.annotation.Location;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class LocationHandlingMethod extends HandlingMethod {
    public LocationHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }

        if (parameter.isAnnotationPresent(Location.class)) {
            return new CompositeParameter(ArgumentType.COORDINATES, parameter);
        }

        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());
        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.UNKNOWN);
        return new CompositeParameter(baseType, parameter);
    }
}
