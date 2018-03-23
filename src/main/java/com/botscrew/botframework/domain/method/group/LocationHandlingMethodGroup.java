package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.annotation.Location;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.LocationHandlingMethod;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.exception.MethodSignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class LocationHandlingMethodGroup implements HandlingMethodGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationHandlingMethodGroup.class);

    private final Map<StateMethodKey, LocationHandlingMethod> instanceMethods;

    public LocationHandlingMethodGroup() {
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
        LocationHandlingMethod byState = instanceMethods.get(key);
        if (byState != null) {
            return Optional.of(byState);
        }

        LocationHandlingMethod defaultMethod = instanceMethods.get(new StateMethodKey(Key.ALL_STATES));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(Location.class)) {
            Location textAnnotation = method.getAnnotation(Location.class);
            List<StateMethodKey> keys = generateKeys(textAnnotation);
            LocationHandlingMethod instanceMethod = new LocationHandlingMethod(object, method);

            for (StateMethodKey key : keys) {
                if (instanceMethods.containsKey(key)) {
                    throw new MethodSignatureException("Defined a few methods with @Location annotation with key: " + key.toString());
                }
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Location handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    private List<StateMethodKey> generateKeys(Location locationAnnotation) {
        List<StateMethodKey> keys = new ArrayList<>();

        if (locationAnnotation.states().length == 0) {
            keys.add(new StateMethodKey(Key.ALL_STATES));
            return keys;
        }

        for (String state : locationAnnotation.states()) {
            keys.add(new StateMethodKey(state));
        }
        return keys;
    }
}
