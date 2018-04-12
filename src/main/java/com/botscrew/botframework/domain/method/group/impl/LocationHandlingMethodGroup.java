package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Location;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.LocationHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LocationHandlingMethodGroup extends StateHandlingMethodGroup {

    public LocationHandlingMethodGroup() {
        super(Location.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Location) annotation).states());
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new LocationHandlingMethod(object, method);
    }
}