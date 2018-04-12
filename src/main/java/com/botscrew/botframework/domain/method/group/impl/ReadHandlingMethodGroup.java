package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Read;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.ReadHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ReadHandlingMethodGroup extends StateHandlingMethodGroup {
    public ReadHandlingMethodGroup() {
        super(Read.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Read) annotation).states());
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new ReadHandlingMethod(object, method);
    }
}
