package com.botscrew.botframework.domain.method;

import java.lang.reflect.Method;

public class DeliveryHandlingMethod extends StateWithOriginalHandlingMethod {
    public DeliveryHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }
}
