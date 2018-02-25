package com.botscrew.botframework.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Component
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ChatEventsProcessor {
}