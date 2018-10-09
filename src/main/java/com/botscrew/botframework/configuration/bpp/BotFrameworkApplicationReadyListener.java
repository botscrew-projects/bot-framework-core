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

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.IntentProcessor;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains logic of looking for beans annotated as `Processors`
 *
 * @see com.botscrew.botframework.annotation.ChatEventsProcessor
 * @see com.botscrew.botframework.annotation.IntentProcessor
 */

public class BotFrameworkApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ApplicationContext applicationContext;

    public BotFrameworkApplicationReadyListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Collection<Object> chatEventProcessors = applicationContext.getBeansWithAnnotation(ChatEventsProcessor.class).values();
        Collection<Object> intentProcessors = applicationContext.getBeansWithAnnotation(IntentProcessor.class).values();


        List<Object> handlingBeans = Stream
                .concat(chatEventProcessors.stream(), intentProcessors.stream())
                .distinct()
                .collect(Collectors.toList());

        Map<String, HandlingMethodGroup> methodGroups = applicationContext.getBeansOfType(HandlingMethodGroup.class);

        for (HandlingMethodGroup group : methodGroups.values()) {
            for (Object handlingBean : handlingBeans) {
                Class<?> userClass = ClassUtils.getUserClass(handlingBean);
                for (Method method : userClass.getMethods()) {
                    Optional<Annotation> applicableAnotation = group.getApplicableAnnotation(method);
                    applicableAnotation.ifPresent(a -> group.register(a, handlingBean, method));
                }
            }
        }
    }
}
