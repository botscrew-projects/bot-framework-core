package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.annotation.Original;
import com.botscrew.botframework.annotation.Postback;
import com.botscrew.botframework.annotation.Referral;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.user.ChatUser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class ReferralHandlingMethod extends HandlingMethod {

    public ReferralHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }
        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }
        if (parameter.isAnnotationPresent(Referral.class)) {
            return new CompositeParameter(ArgumentType.REFERRAL, parameter);
        }
        if (parameter.isAnnotationPresent(Original.class)) {
            return new CompositeParameter(ArgumentType.ORIGINAL_RESPONSE, parameter);
        }
        if (parameter.isAnnotationPresent(Postback.class)) {
            return new CompositeParameter(ArgumentType.POSTBACK, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.COMPLEX_TYPE);
        return new CompositeParameter(baseType, parameter);
    }
}
