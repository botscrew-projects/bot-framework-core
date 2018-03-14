package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.IntentProcessor;
import com.botscrew.botframework.container.IntentMethodGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class IntentProcessorAnnotationBPP implements BeanPostProcessor {
    private IntentMethodGroup intentMethodGroup;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        if (o instanceof IntentMethodGroup) {
            intentMethodGroup = (IntentMethodGroup) o;
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (o.getClass().isAnnotationPresent(IntentProcessor.class)) {
           intentMethodGroup.register(o);
        }
        return o;
    }
}
