package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Postback;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.PostbackHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateAndValueHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PostbackHandlingMethodGroup extends StateAndValueHandlingMethodGroup {
    public PostbackHandlingMethodGroup() {
        super(Postback.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Postback) annotation).states());
    }

    @Override
    public String getValue(Annotation annotation) {
        return ((Postback) annotation).value();
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new PostbackHandlingMethod(object, method);
    }
}
