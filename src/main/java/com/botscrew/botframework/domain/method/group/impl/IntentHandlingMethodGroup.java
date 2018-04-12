package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.IntentHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateAndValueHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class IntentHandlingMethodGroup extends StateAndValueHandlingMethodGroup {
    public IntentHandlingMethodGroup() {
        super(Intent.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Intent) annotation).states());
    }

    @Override
    public String getValue(Annotation annotation) {
        return ((Intent) annotation).value();
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new IntentHandlingMethod(object, method);
    }
}
