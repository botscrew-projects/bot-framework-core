package com.botscrew.botframework.configuration.bpp;

import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class SpecificHandlingMethodAnnotationBPP implements BeanPostProcessor {
    private final Class<? extends Annotation> annotation;

    private List<HandlingMethodGroup> handlingMethodGroups;
    private List<Object> processors;

    public SpecificHandlingMethodAnnotationBPP(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        handlingMethodGroups = new ArrayList<>();
        processors = new ArrayList<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (o instanceof HandlingMethodGroup) {
            HandlingMethodGroup handlingMethodGroup = (HandlingMethodGroup) o;
            handlingMethodGroups.add(handlingMethodGroup);
            processors.forEach(handlingMethodGroup::register);
            return o;
        }
        if (o.getClass().isAnnotationPresent(annotation)) {
            processors.add(o);
            handlingMethodGroups.forEach(hmg -> hmg.register(o));
        }
        return o;
    }
}
