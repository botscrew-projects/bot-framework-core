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

package com.botscrew.botframework.configuration;

import com.botscrew.botframework.configuration.listener.BotFrameworkApplicationReadyListener;
import com.botscrew.botframework.container.*;
import com.botscrew.botframework.domain.method.group.impl.*;
import com.botscrew.botframework.sender.PlatformSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Contains all bean definitions needed for Bot Framework
 */
@Configuration
public class BotFrameworkConfiguration {

    @Bean
    public BotFrameworkApplicationReadyListener botFrameworkApplicationReadyListener(ApplicationContext context) {
        return new BotFrameworkApplicationReadyListener(context);
    }

    @Bean
    @ConditionalOnMissingBean(PostbackHandlingMethodGroup.class)
    public PostbackHandlingMethodGroup postbackHandlingMethodGroup() {
        return new PostbackHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(PostbackContainer.class)
    public PostbackContainer postbackContainer(PostbackHandlingMethodGroup postbackHandlingMethodGroup) {
        return new PostbackContainer(postbackHandlingMethodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(TextHandlingMethodGroup.class)
    public TextHandlingMethodGroup textHandlingMethodGroup() {
        return new TextHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(TextContainer.class)
    public TextContainer textContainer(TextHandlingMethodGroup methodGroup) {
        return new TextContainer(methodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(ReadHandlingMethodGroup.class)
    public ReadHandlingMethodGroup readHandlingMethodGroup() {
        return new ReadHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(ReadContainer.class)
    public ReadContainer readContainer(ReadHandlingMethodGroup methodGroup) {
        return new ReadContainer(methodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(EchoHandlingMethodGroup.class)
    public EchoHandlingMethodGroup echoHandlingMethodGroup() {
        return new EchoHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(EchoContainer.class)
    public EchoContainer echoContainer(EchoHandlingMethodGroup methodGroup) {
        return new EchoContainer(methodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(DeliveryHandlingMethodGroup.class)
    public DeliveryHandlingMethodGroup deliveryHandlingMethodGroup() {
        return new DeliveryHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(DeliveryContainer.class)
    public DeliveryContainer deliveryContainer(DeliveryHandlingMethodGroup methodGroup) {
        return new DeliveryContainer(methodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(ReferralHandlingMethodGroup.class)
    public ReferralHandlingMethodGroup referralHandlingMethodGroup() {
        return new ReferralHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(ReferralContainer.class)
    public ReferralContainer referralContainer(ReferralHandlingMethodGroup methodGroup) {
        return new ReferralContainer(methodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(LocationHandlingMethodGroup.class)
    public LocationHandlingMethodGroup locationHandlingMethodGroup() {
        return new LocationHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(LocationContainer.class)
    public LocationContainer locationContainer(LocationHandlingMethodGroup locationHandlingMethodGroup) {
        return new LocationContainer(locationHandlingMethodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(IntentHandlingMethodGroup.class)
    public IntentHandlingMethodGroup intentMethodGroup() {
        return new IntentHandlingMethodGroup();
    }

    @Bean
    @ConditionalOnMissingBean(IntentContainer.class)
    public IntentContainer intentContainer(IntentHandlingMethodGroup intentMethodGroup) {
        return new IntentContainer(intentMethodGroup);
    }

    @Bean
    @ConditionalOnMissingBean(PlatformSender.class)
    public PlatformSender platformSender() {
        return new PlatformSender();
    }

    @Bean
    public ThreadPoolTaskScheduler senderTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.initialize();
        return scheduler;
    }
}
