package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.ChatEventsProcessor;

public class ChatEventsProcessorAnnotationBPP extends SpecificHandlingMethodAnnotationBPP {
    public ChatEventsProcessorAnnotationBPP() {
        super(ChatEventsProcessor.class);
    }
}
