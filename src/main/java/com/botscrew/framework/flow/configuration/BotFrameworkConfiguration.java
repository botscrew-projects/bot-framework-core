package com.botscrew.framework.flow.configuration;

import com.botscrew.framework.flow.configuration.bpp.PostbackContainerBPP;
import com.botscrew.framework.flow.container.LocationContainer;
import com.botscrew.framework.flow.container.PostbackContainer;
import com.botscrew.framework.flow.container.TextContainer;
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
