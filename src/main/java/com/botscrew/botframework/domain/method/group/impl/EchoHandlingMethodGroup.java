package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Echo;
import com.botscrew.botframework.domain.method.EchoHandlingMethod;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class EchoHandlingMethodGroup extends StateHandlingMethodGroup {
    public EchoHandlingMethodGroup() {
        super(Echo.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Echo) annotation).states());
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new EchoHandlingMethod(object, method);
    }
}