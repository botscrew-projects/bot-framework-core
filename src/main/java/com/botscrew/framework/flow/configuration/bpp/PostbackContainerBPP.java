package com.botscrew.framework.flow.configuration.bpp;

import com.botscrew.framework.flow.annotation.ChatActionsProcessor;
import com.botscrew.framework.flow.container.AbstractContainer;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class PostbackContainerBPP implements BeanPostProcessor {

    private List<AbstractContainer> containers;
    private List<Object> messagingEventsProcessors;

    public PostbackContainerBPP() {
        messagingEventsProcessors = new ArrayList<>();
        this.containers = new ArrayList<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) {
        if (o instanceof AbstractContainer) {
            AbstractContainer casted = (AbstractContainer) o;
            containers.add(casted);

            for (Object messagingEventsProcessor : messagingEventsProcessors) {
                casted.register(messagingEventsProcessor);
            }
        }

        if (o.getClass().isAnnotationPresent(ChatActionsProcessor.class)) {
            messagingEventsProcessors.add(o);
            for (AbstractContainer container : containers) {
                container.register(o);
            }
        }
        return o;
    }
}