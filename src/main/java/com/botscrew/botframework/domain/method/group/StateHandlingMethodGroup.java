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
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.exception.MethodSignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Describes group of methods-handlers which are responsible for handling some type of events
 */
public abstract class StateHandlingMethodGroup implements HandlingMethodGroup<StateMethodKey> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateHandlingMethodGroup.class);

    private final Class<? extends Annotation> annotationType;
    private final Map<StateMethodKey, HandlingMethod> instanceMethods;

    /**
     * @param annotationType Annotation of event which this group handles(f.e. {@link com.botscrew.botframework.annotation.Text}
     */
    public StateHandlingMethodGroup(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        instanceMethods = new HashMap<>();
    }

    public abstract List<String> getStates(Annotation annotation);

    public abstract HandlingMethod createHandlingMethod(Object object, Method method);

    @Override
    public Optional<Annotation> getApplicableAnnotation(Method method) {
        if (method.isAnnotationPresent(annotationType)) {
            return Optional.of(method.getAnnotation(annotationType));
        } else return Optional.empty();
    }

    @Override
    public void register(Annotation annotation, Object instance, Method method) {
        if (method.isAnnotationPresent(annotationType)) {
            List<StateMethodKey> keys = generateKeys(getStates(annotation));
            HandlingMethod instanceMethod = createHandlingMethod(instance, method);

            for (StateMethodKey key : keys) {
                if (instanceMethods.containsKey(key)) {
                    throw new MethodSignatureException(
                            String.format("Defined a few methods with %s annotation with key: %s", annotation.getClass().toString(), key.toString()));
                }
                instanceMethods.put(key, instanceMethod);
            }
            LOGGER.debug("Handler registered: " + instance.getClass().getName() + " -> " + method.getName() + "()");
        }
    }

    /**
     * Gives you ability to look for the appropriate method
     */
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
            LOGGER.debug("Handler registered: " + object.getClass().getName() + " -> " + method.getName() + "()");
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
