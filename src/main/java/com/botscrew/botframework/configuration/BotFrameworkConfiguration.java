package com.botscrew.botframework.configuration;

import com.botscrew.botframework.configuration.bpp.ChatEventsAnnotationBPP;
import com.botscrew.botframework.configuration.bpp.IntentProcessorAnnotationBPP;
import com.botscrew.botframework.container.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotFrameworkConfiguration {

    @Bean
    public ChatEventsAnnotationBPP postbackContainerBPP() {
        return new ChatEventsAnnotationBPP();
    }

    @Bean
    public IntentProcessorAnnotationBPP intentProcessorAnnotationBPP() {
        return new IntentProcessorAnnotationBPP();
    }

    @Bean
    public PostbackContainer postbackContainer() {
        return new PostbackContainer();
    }

    @Bean
    public TextContainer textContainer() {
        return new TextContainer();
    }

    @Bean
    public LocationContainer locationContainer() {
        return new LocationContainer();
    }

    @Bean
    public IntentMethodGroup intentMethodGroup() {
        return new IntentMethodGroup();
    }

    @Bean
    public IntentContainer intentContainer(IntentMethodGroup intentMethodGroup) {
        return new IntentContainer(intentMethodGroup);
    }
}
