package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class SpecificHandlingMethodAnnotationBPP implements BeanPostProcessor {
    private final Class<? extends Annotation> annotation;

    private HandlingMethodGroup handlingMethodGroup;
    private List<Object> intentProcessors = new ArrayList<>();

    public SpecificHandlingMethodAnnotationBPP(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (handlingMethodGroup != null) {
            handlingMethodGroup.register(o);
            return o;
        }

        if (o instanceof IntentHandlingMethodGroup) {
            handlingMethodGroup = (IntentHandlingMethodGroup) o;
            intentProcessors.forEach(p -> handlingMethodGroup.register(p));
            return o;
        }

        if (o.getClass().isAnnotationPresent(annotation)) {
            intentProcessors.add(o);
        }
        return o;
    }
}
