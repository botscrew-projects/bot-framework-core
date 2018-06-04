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

import com.botscrew.botframework.configuration.bpp.ChatEventsProcessorAnnotationBPP;
import com.botscrew.botframework.configuration.bpp.IntentProcessorAnnotationBPP;
import com.botscrew.botframework.container.*;
import com.botscrew.botframework.domain.method.group.impl.*;
import com.botscrew.botframework.sender.PlatformSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contains all bean definitions needed for Bot Framework
 */
@Configuration
public class BotFrameworkConfiguration {

    @Bean
    public ChatEventsProcessorAnnotationBPP postbackContainerBPP() {
        return new ChatEventsProcessorAnnotationBPP();
    }

    @Bean
    public IntentProcessorAnnotationBPP intentProcessorAnnotationBPP() {
        return new IntentProcessorAnnotationBPP();
    }

    @Bean
    public PostbackHandlingMethodGroup postbackHandlingMethodGroup() {
        return new PostbackHandlingMethodGroup();
    }

    @Bean
    public PostbackContainer postbackContainer(PostbackHandlingMethodGroup postbackHandlingMethodGroup) {
        return new PostbackContainer(postbackHandlingMethodGroup);
    }

    @Bean
    public TextHandlingMethodGroup textHandlingMethodGroup() {
        return new TextHandlingMethodGroup();
    }

    @Bean
    public TextContainer textContainer(TextHandlingMethodGroup methodGroup) {
        return new TextContainer(methodGroup);
    }

    @Bean
    public ReadHandlingMethodGroup readHandlingMethodGroup() {
        return new ReadHandlingMethodGroup();
    }

    @Bean
    public ReadContainer readContainer(ReadHandlingMethodGroup methodGroup) {
        return new ReadContainer(methodGroup);
    }

    @Bean
    public EchoHandlingMethodGroup echoHandlingMethodGroup() {
        return new EchoHandlingMethodGroup();
    }

    @Bean
    public EchoContainer echoContainer(EchoHandlingMethodGroup methodGroup) {
        return new EchoContainer(methodGroup);
    }

    @Bean
    public DeliveryHandlingMethodGroup deliveryHandlingMethodGroup() {
        return new DeliveryHandlingMethodGroup();
    }

    @Bean
    public DeliveryContainer deliveryContainer(DeliveryHandlingMethodGroup methodGroup) {
        return new DeliveryContainer(methodGroup);
    }

    @Bean
    public ReferralHandlingMethodGroup referralHandlingMethodGroup() {
        return new ReferralHandlingMethodGroup();
    }

    @Bean
    public ReferralContainer referralContainer(ReferralHandlingMethodGroup methodGroup) {
        return new ReferralContainer(methodGroup);
    }

    @Bean
    public LocationHandlingMethodGroup locationHandlingMethodGroup() {
        return new LocationHandlingMethodGroup();
    }

    @Bean
    public LocationContainer locationContainer(LocationHandlingMethodGroup locationHandlingMethodGroup) {
        return new LocationContainer(locationHandlingMethodGroup);
    }

    @Bean
    public IntentHandlingMethodGroup intentMethodGroup() {
        return new IntentHandlingMethodGroup();
    }

    @Bean
    public IntentContainer intentContainer(IntentHandlingMethodGroup intentMethodGroup) {
        return new IntentContainer(intentMethodGroup);
    }

    @Bean
    public PlatformSender platformSender() {
        return new PlatformSender();
    }
}
