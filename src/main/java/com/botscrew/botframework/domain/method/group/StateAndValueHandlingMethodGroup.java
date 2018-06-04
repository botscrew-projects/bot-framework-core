/*
 * Copyright 2018 BotsCrew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.key.MethodKey;
import com.botscrew.botframework.domain.method.key.StateAndValueMethodKey;
import com.botscrew.botframework.exception.MethodSignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Describes group of methods-handlers of one type based on user state and some additional value(f.e. methods with @Postback annotation)
 */
public abstract class StateAndValueHandlingMethodGroup implements HandlingMethodGroup<StateAndValueMethodKey> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateAndValueHandlingMethodGroup.class);

    private final Class<? extends Annotation> annotationType;
    private final Map<MethodKey, HandlingMethod> instanceMethods;

    public StateAndValueHandlingMethodGroup(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        instanceMethods = new HashMap<>();
    }

    public abstract List<String> getStates(Annotation annotation);

    public abstract String getValue(Annotation annotation);

    public abstract HandlingMethod createHandlingMethod(Object object, Method method);

    @Override
    public void register(Object object) {
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            registerIfAnnotationPresent(object, method);
        }
    }

    @Override
    public Optional<HandlingMethod> find(StateAndValueMethodKey key) {
        HandlingMethod byPostbackAndState = instanceMethods.get(key);
        if (byPostbackAndState != null) {
            return Optional.of(byPostbackAndState);
        }

        HandlingMethod byPostback = instanceMethods.get(new StateAndValueMethodKey(HandlingMethodGroup.Key.ALL, key.getStringKey()));
        if (byPostback != null) {
            return Optional.of(byPostback);
        }

        HandlingMethod byState = instanceMethods.get(new StateAndValueMethodKey(key.getState(), HandlingMethodGroup.Key.ALL));
        if (byState != null) {
            return Optional.of(byState);
        }

        HandlingMethod defaultMethod = instanceMethods.get(new StateAndValueMethodKey(HandlingMethodGroup.Key.ALL, HandlingMethodGroup.Key.ALL));
        return Optional.ofNullable(defaultMethod);
    }

    private void registerIfAnnotationPresent(Object object, Method method) {
        if (method.isAnnotationPresent(annotationType)) {
            Annotation annotation = method.getAnnotation(annotationType);
            List<StateAndValueMethodKey> keys = generateKeys(getStates(annotation), getValue(annotation));
            HandlingMethod instanceMethod = createHandlingMethod(object, method);

            for (StateAndValueMethodKey key : keys) {
                if (instanceMethods.containsKey(key)) {
                    throw new MethodSignatureException(
                            String.format("Defined a few methods with %s annotation with key: %s", annotation.getClass().toString(), key.toString()));
                }
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Postback handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    private List<StateAndValueMethodKey> generateKeys(List<String> states, String value) {
        List<StateAndValueMethodKey> keys = new ArrayList<>();

        String key = value.isEmpty() ? Key.ALL : value;

        if (states.isEmpty()) {
            keys.add(new StateAndValueMethodKey(HandlingMethodGroup.Key.ALL, key));
            return keys;
        }

        for (String state : states) {
            keys.add(new StateAndValueMethodKey(state, key));
        }

        return keys;
    }
}

