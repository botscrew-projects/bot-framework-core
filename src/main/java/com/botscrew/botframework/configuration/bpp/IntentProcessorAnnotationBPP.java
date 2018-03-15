package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.annotation.IntentProcessor;
import com.botscrew.botframework.container.IntentMethodGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class IntentProcessorAnnotationBPP implements BeanPostProcessor {
    private IntentMethodGroup intentMethodGroup;
    private List<Object> intentProcessors = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (intentMethodGroup != null) {
            intentMethodGroup.register(o);
            return o;
        }

        if (o instanceof IntentMethodGroup) {
            intentMethodGroup = (IntentMethodGroup) o;
            intentProcessors.forEach(p -> intentMethodGroup.register(p));
            return o;
        }

        if (o.getClass().isAnnotationPresent(IntentProcessor.class)) {
            intentProcessors.add(o);
        }
        return o;
    }
}
