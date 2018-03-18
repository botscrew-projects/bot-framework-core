package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.annotation.Text;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.TextHandlingMethod;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class TextHandlingMethodGroup implements HandlingMethodGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextHandlingMethodGroup.class);

    private final Map<StateMethodKey, TextHandlingMethod> instanceMethods;

    public TextHandlingMethodGroup() {
        instanceMethods = new HashMap<>();
    }

    @Override
    public void register(Object object) {
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            registerIfAnnotationPresent(object, method);
        }
    }

    public Optional<HandlingMethod> find(StateMethodKey key) {
        TextHandlingMethod byState = instanceMethods.get(key);
        if (byState != null) {
            return Optional.of(byState);
        }

        TextHandlingMethod defaultMethod = instanceMethods.get(new StateMethodKey(ALL_STATES));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(Text.class)) {
            Text textAnnotation = method.getAnnotation(Text.class);
            List<StateMethodKey> keys = generateKeys(textAnnotation);
            TextHandlingMethod instanceMethod = new TextHandlingMethod(object, method);

            for (StateMethodKey key : keys) {
                instanceMethods.put(key, instanceMethod);
                LOGGER.debug("Text handling method added with key: {}", key);
            }
        }
    }

    private List<StateMethodKey> generateKeys(Text textAnnotation) {
        List<StateMethodKey> keys = new ArrayList<>();

        if (textAnnotation.states().length == 0) {
            keys.add(new StateMethodKey(ALL_STATES));
            return keys;
        }

        for (String state : textAnnotation.states()) {
            keys.add(new StateMethodKey(state));
        }
        return keys;
    }
}
