package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.impl.TextHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.exception.MethodSignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public abstract class StateHandlingMethodGroup implements HandlingMethodGroup<StateMethodKey> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextHandlingMethodGroup.class);

    private final Class<? extends Annotation> annotationType;
    private final Map<StateMethodKey, HandlingMethod> instanceMethods;

    public StateHandlingMethodGroup(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        instanceMethods = new HashMap<>();
    }

    public abstract List<String> getStates(Annotation annotation);
    public abstract HandlingMethod createHandlingMethod(Object object, Method method);

    @Override
    public void register(Object object) {
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            registerIfAnnotationPresent(object, method);
        }
    }

    @Override
    public Optional<HandlingMethod> find(StateMethodKey key) {
        HandlingMethod byState = instanceMethods.get(key);
        if (byState != null) {
            return Optional.of(byState);
        }

        HandlingMethod defaultMethod = instanceMethods.get(new StateMethodKey(HandlingMethodGroup.Key.ALL));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(annotationType)) {
            Annotation annotation = method.getAnnotation(annotationType);
            List<StateMethodKey> keys = generateKeys(getStates(annotation));
            HandlingMethod instanceMethod = createHandlingMethod(object, method);

            for (StateMethodKey key : keys) {
                if (instanceMethods.containsKey(key)) {
                    throw new MethodSignatureException(String.format("Defined a few methods with %s annotation with key: %s",
                                    annotation.getClass().toString(), key.toString()));
                }
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Text handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    private List<StateMethodKey> generateKeys(List<String> states) {
        List<StateMethodKey> keys = new ArrayList<>();

        if (states.isEmpty()) {
            keys.add(new StateMethodKey(HandlingMethodGroup.Key.ALL));
            return keys;
        }

        for (String state : states) {
            keys.add(new StateMethodKey(state));
        }
        return keys;
    }
}
