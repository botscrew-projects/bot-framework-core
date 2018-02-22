package com.botscrew.botframework.configuration;

import com.botscrew.botframework.configuration.bpp.PostbackContainerBPP;
import com.botscrew.botframework.container.LocationContainer;
import com.botscrew.botframework.container.PostbackContainer;
import com.botscrew.botframework.container.TextContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotFrameworkConfiguration {

    @Bean
    public PostbackContainerBPP postbackContainerBPP() {
        return new PostbackContainerBPP();
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
}
