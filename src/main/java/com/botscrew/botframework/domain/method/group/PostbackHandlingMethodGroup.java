package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.annotation.Postback;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.PostbackHandlingMethod;
import com.botscrew.botframework.domain.method.key.BiMethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class PostbackHandlingMethodGroup implements HandlingMethodGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntentHandlingMethodGroup.class);

    private final Map<BiMethodKey, PostbackHandlingMethod> instanceMethods;

    public PostbackHandlingMethodGroup() {
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
        PostbackHandlingMethod byPostbackAndState = instanceMethods.get(key);
        if (byPostbackAndState != null) {
            return Optional.of(byPostbackAndState);
        }

        PostbackHandlingMethod byPostback = instanceMethods.get(new BiMethodKey(Key.ALL_STATES, key.getStringKey()));
        if (byPostback != null) {
            return Optional.of(byPostback);
        }

        PostbackHandlingMethod byState = instanceMethods.get(new BiMethodKey(key.getState(), Key.ALL_POSTBACKS));
        if (byState != null) {
            return Optional.of(byState);
        }

        PostbackHandlingMethod defaultMethod = instanceMethods.get(new BiMethodKey(Key.ALL_STATES, Key.ALL_POSTBACKS));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(Postback.class)) {
            Postback postackAnnotation = method.getAnnotation(Postback.class);
            List<BiMethodKey> keys = generateKeys(postackAnnotation);
            PostbackHandlingMethod instanceMethod = new PostbackHandlingMethod(object, method);

            for (BiMethodKey key : keys) {
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Postback handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    private List<BiMethodKey> generateKeys(Postback postbackAnnotation) {
        List<BiMethodKey> keys = new ArrayList<>();

        String postback = postbackAnnotation.value();
        if (postback.isEmpty()) postback = Key.ALL_POSTBACKS;

        if (postbackAnnotation.states().length == 0) {
            keys.add(new BiMethodKey(Key.ALL_STATES, postback));
            return keys;
        }

        for (String state : postbackAnnotation.states()) {
            keys.add(new BiMethodKey(state, postback));
        }

        return keys;
    }
}
