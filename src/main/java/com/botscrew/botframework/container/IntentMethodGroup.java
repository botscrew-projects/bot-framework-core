package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.model.IntentInstanceMethod;
import com.botscrew.botframework.model.IntentMethodKey;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class IntentMethodGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentMethodGroup.class);

    public static final String ALL_INTENTS = "ALL_INTENTS";
    public static final String ALL_STATES = "ALL_STATES";

    private final Map<IntentMethodKey, IntentInstanceMethod> instanceMethods;

    public IntentMethodGroup() {
        instanceMethods = new HashMap<>();
    }

    public void register(Object object) {
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            registerIfAnnotationPresent(object, method);
        }
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(Intent.class)) {
            Intent intentAnnotation = method.getAnnotation(Intent.class);
            List<IntentMethodKey> keys = generateKeys(intentAnnotation);
            IntentInstanceMethod instanceMethod = generateInstanceMethod(object, method);

            for (IntentMethodKey key : keys) {
                instanceMethods.put(key, instanceMethod);
                LOGGER.info("Intent key added: " + key.toString());
            }
        }
    }

    private IntentInstanceMethod generateInstanceMethod(Object object, Method method) {
        return new IntentInstanceMethod(object, method);
    }

    private List<IntentMethodKey> generateKeys(Intent intentAnnotation) {
        List<IntentMethodKey> keys = new ArrayList<>();

        String intent = intentAnnotation.name();
        if (intent.isEmpty()) intent = ALL_INTENTS;

        if (intentAnnotation.states().length == 0) {
            keys.add(new IntentMethodKey(ALL_STATES, intent));
            return keys;
        }

        for (String state : intentAnnotation.states()) {
            keys.add(new IntentMethodKey(state, intent));
        }

        return keys;
    }

    public Optional<IntentInstanceMethod> find(IntentMethodKey key) {
        IntentInstanceMethod byIntentAndState = instanceMethods.get(key);
        if (byIntentAndState != null) {
            return Optional.of(byIntentAndState);
        }

        IntentInstanceMethod byIntent = instanceMethods.get(new IntentMethodKey(ALL_STATES, key.getIntent()));
        if (byIntent != null) {
            return Optional.of(byIntent);
        }

        IntentInstanceMethod byState = instanceMethods.get(new IntentMethodKey(key.getState(), ALL_INTENTS));
        if (byState != null) {
            return Optional.of(byState);
        }

        IntentInstanceMethod defaultMethod = instanceMethods.get(new IntentMethodKey(ALL_STATES, ALL_INTENTS));
        return Optional.ofNullable(defaultMethod);
    }
}
