package com.botscrew.botframework.domain.method;

import java.lang.reflect.Method;

public class EchoHandlingMethod extends StateWithOriginalHandlingMethod {
    public EchoHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }
}
