package com.botscrew.botframework.configuration;

import com.botscrew.botframework.configuration.bpp.ChatEventsAnnotationBPP;
import com.botscrew.botframework.configuration.bpp.IntentProcessorAnnotationBPP;
import com.botscrew.botframework.container.*;
import com.botscrew.botframework.domain.ArgumentsComposer;
import com.botscrew.botframework.domain.ArgumentsComposerFactory;
import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Import(ArgumentConvertersConfiguration.class)
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
