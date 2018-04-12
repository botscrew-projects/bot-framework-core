package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Delivery;
import com.botscrew.botframework.domain.method.DeliveryHandlingMethod;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class DeliveryHandlingMethodGroup extends StateHandlingMethodGroup {
    public DeliveryHandlingMethodGroup() {
        super(Delivery.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Delivery) annotation).states());
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new DeliveryHandlingMethod(object, method);
    }
}