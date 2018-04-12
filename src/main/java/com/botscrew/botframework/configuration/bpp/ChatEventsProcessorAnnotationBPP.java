package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.domain.method.group.impl.*;

import java.util.Arrays;

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
