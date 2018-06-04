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
import com.botscrew.botframework.domain.method.group.impl.*;

import java.util.Arrays;

/**
 * Responsible for looking for beans annotated with {@link ChatEventsProcessor}
 * @see ChatEventsProcessor
 */
public class ChatEventsProcessorAnnotationBPP extends SpecificHandlingMethodAnnotationBPP {
    public ChatEventsProcessorAnnotationBPP() {
        super(ChatEventsProcessor.class, Arrays.asList(
                TextHandlingMethodGroup.class,
                PostbackHandlingMethodGroup.class,
                LocationHandlingMethodGroup.class,
                ReferralHandlingMethodGroup.class,
                ReadHandlingMethodGroup.class,
                EchoHandlingMethodGroup.class,
                DeliveryHandlingMethodGroup.class
        ));
    }
}
