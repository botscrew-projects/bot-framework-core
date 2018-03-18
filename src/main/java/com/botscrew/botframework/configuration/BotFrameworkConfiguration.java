package com.botscrew.botframework.configuration;

import com.botscrew.botframework.configuration.bpp.ChatEventsProcessorAnnotationBPP;
import com.botscrew.botframework.configuration.bpp.IntentProcessorAnnotationBPP;
import com.botscrew.botframework.container.IntentContainer;
import com.botscrew.botframework.container.LocationContainer;
import com.botscrew.botframework.container.PostbackContainer;
import com.botscrew.botframework.container.TextContainer;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.LocationHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.PostbackHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.TextHandlingMethodGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotFrameworkConfiguration {

    @Bean
    public ChatEventsProcessorAnnotationBPP postbackContainerBPP() {
        return new ChatEventsProcessorAnnotationBPP();
    }

    @Bean
    public IntentProcessorAnnotationBPP intentProcessorAnnotationBPP() {
        return new IntentProcessorAnnotationBPP();
    }

    @Bean
    public PostbackHandlingMethodGroup postbackHandlingMethodGroup() {
        return new PostbackHandlingMethodGroup();
    }

    @Bean
    public PostbackContainer postbackContainer(PostbackHandlingMethodGroup postbackHandlingMethodGroup) {
        return new PostbackContainer(postbackHandlingMethodGroup);
    }

    @Bean
    public TextHandlingMethodGroup textHandlingMethodGroup() {
        return new TextHandlingMethodGroup();
    }

    @Bean
    public TextContainer textContainer(TextHandlingMethodGroup methodGroup) {
        return new TextContainer(methodGroup);
    }

    @Bean
    public LocationHandlingMethodGroup locationHandlingMethodGroup() {
        return new LocationHandlingMethodGroup();
    }

    @Bean
    public LocationContainer locationContainer(LocationHandlingMethodGroup locationHandlingMethodGroup) {
        return new LocationContainer(locationHandlingMethodGroup);
    }

    @Bean
    public IntentHandlingMethodGroup intentMethodGroup() {
        return new IntentHandlingMethodGroup();
    }

    @Bean
    public IntentContainer intentContainer(IntentHandlingMethodGroup intentMethodGroup) {
        return new IntentContainer(intentMethodGroup);
    }
}
