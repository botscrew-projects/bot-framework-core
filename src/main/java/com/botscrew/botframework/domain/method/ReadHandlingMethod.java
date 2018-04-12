package com.botscrew.botframework.domain.method;

import java.lang.reflect.Method;

public class ReadHandlingMethod extends StateWithOriginalHandlingMethod {

    public ReadHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }
}
