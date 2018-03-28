package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.IntentProcessor;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;

import java.util.Collections;

public class IntentProcessorAnnotationBPP extends SpecificHandlingMethodAnnotationBPP {

    public IntentProcessorAnnotationBPP() {
        super(IntentProcessor.class, Collections.singletonList(IntentHandlingMethodGroup.class));
    }
}
