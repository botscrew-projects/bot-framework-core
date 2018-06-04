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

package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains logic of looking for beans annotated as `Processors`
 * @see com.botscrew.botframework.annotation.ChatEventsProcessor
 * @see com.botscrew.botframework.annotation.IntentProcessor
 */
public abstract class SpecificHandlingMethodAnnotationBPP implements BeanPostProcessor {
    /**
     * Class-level annotation. BPP will look for classes annotated with it.
     */
    private final Class<? extends Annotation> annotation;
    /**
     * List of handling-method groups types available for registering annotated methods
     */
    private final List<Class<? extends HandlingMethodGroup>> handlingMethodGroupTypes;

    /**
     * List of handling method groups found during startup, where we will register found annotated beans
     */
    private List<HandlingMethodGroup> handlingMethodGroups;
    /**
     * List of annotated beans
     */
    private List<Object> processors;

    public SpecificHandlingMethodAnnotationBPP(Class<? extends Annotation> annotation, List<Class<? extends HandlingMethodGroup>> handlingMethodGroupTypes) {
        this.annotation = annotation;
        this.handlingMethodGroupTypes = handlingMethodGroupTypes;
        handlingMethodGroups = new ArrayList<>();
        processors = new ArrayList<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (o instanceof HandlingMethodGroup && handlingMethodGroupTypes.contains(o.getClass())) {
            HandlingMethodGroup handlingMethodGroup = (HandlingMethodGroup) o;
            handlingMethodGroups.add(handlingMethodGroup);
            processors.forEach(handlingMethodGroup::register);
            return o;
        }
        if (o.getClass().isAnnotationPresent(annotation)) {
            processors.add(o);
            handlingMethodGroups.forEach(hmg -> hmg.register(o));
        }
        return o;
    }
}
