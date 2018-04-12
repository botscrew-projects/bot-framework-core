package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Text;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.TextHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TextHandlingMethodGroup extends StateHandlingMethodGroup {

    public TextHandlingMethodGroup() {
        super(Text.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Text) annotation).states());
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new TextHandlingMethod(object, method);
    }
}
