package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.domain.method.group.LocationHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.PostbackHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.TextHandlingMethodGroup;

import java.util.Arrays;

public class ChatEventsProcessorAnnotationBPP extends SpecificHandlingMethodAnnotationBPP {
    public ChatEventsProcessorAnnotationBPP() {
        super(ChatEventsProcessor.class, Arrays.asList(
                TextHandlingMethodGroup.class,
                PostbackHandlingMethodGroup.class,
                LocationHandlingMethodGroup.class
        ));
    }
}
