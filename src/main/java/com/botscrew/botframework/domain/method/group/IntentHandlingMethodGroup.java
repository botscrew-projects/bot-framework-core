package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.IntentHandlingMethod;
import com.botscrew.botframework.domain.method.key.BiMethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class IntentHandlingMethodGroup implements HandlingMethodGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentHandlingMethodGroup.class);

    private final Map<BiMethodKey, IntentHandlingMethod> instanceMethods;

    public IntentHandlingMethodGroup() {
        instanceMethods = new HashMap<>();
    }

    @Override
    public void register(Object object) {
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            registerIfAnnotationPresent(object, method);
        }
    }

    public Optional<HandlingMethod> find(BiMethodKey key) {
        IntentHandlingMethod byIntentAndState = instanceMethods.get(key);
        if (byIntentAndState != null) {
            return Optional.of(byIntentAndState);
        }

        IntentHandlingMethod byIntent = instanceMethods.get(new BiMethodKey(Key.ALL_STATES, key.getStringKey()));
        if (byIntent != null) {
            return Optional.of(byIntent);
        }

        IntentHandlingMethod byState = instanceMethods.get(new BiMethodKey(key.getState(), Key.ALL_INTENTS));
        if (byState != null) {
            return Optional.of(byState);
        }

        IntentHandlingMethod defaultMethod = instanceMethods.get(new BiMethodKey(Key.ALL_STATES, Key.ALL_INTENTS));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(Intent.class)) {
            Intent intentAnnotation = method.getAnnotation(Intent.class);
            List<BiMethodKey> keys = generateKeys(intentAnnotation);
            IntentHandlingMethod instanceMethod = new IntentHandlingMethod(object, method);

            for (BiMethodKey key : keys) {
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Intent handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    private List<BiMethodKey> generateKeys(Intent intentAnnotation) {
        List<BiMethodKey> keys = new ArrayList<>();

        String intent = intentAnnotation.value();
        if (intent.isEmpty()) intent = Key.ALL_INTENTS;

        if (intentAnnotation.states().length == 0) {
            keys.add(new BiMethodKey(Key.ALL_STATES, intent));
            return keys;
        }

        for (String state : intentAnnotation.states()) {
            keys.add(new BiMethodKey(state, intent));
        }

        return keys;
    }
}
