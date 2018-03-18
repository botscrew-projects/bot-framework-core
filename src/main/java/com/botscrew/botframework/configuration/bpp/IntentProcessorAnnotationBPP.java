package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.IntentProcessor;

public class IntentProcessorAnnotationBPP extends SpecificHandlingMethodAnnotationBPP {

    public IntentProcessorAnnotationBPP() {
        super(IntentProcessor.class);
    }
}
