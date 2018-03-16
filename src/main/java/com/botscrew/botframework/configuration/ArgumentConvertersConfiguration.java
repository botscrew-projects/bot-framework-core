package com.botscrew.botframework.configuration;

import com.botscrew.botframework.domain.ArgumentsComposerFactory;
import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.StringToDoubleConverter;
import com.botscrew.botframework.domain.converter.impl.StringToIntegerConverter;
import com.botscrew.botframework.domain.converter.impl.UserConverter;
import com.botscrew.botframework.model.ChatUser;
import org.springframework.context.annotation.Bean;

public class ArgumentConvertersConfiguration {

    @Bean
    public ArgumentConverter<ChatUser> userArgumentConverter() {
        UserConverter userConverter = new UserConverter();
        ArgumentsComposerFactory.putConverter(userConverter);
        return userConverter;
    }

    @Bean
    public ArgumentConverter<String> stringToIntegerConverter() {
        StringToIntegerConverter stringToIntegerConverter = new StringToIntegerConverter();
        ArgumentsComposerFactory.putConverter(stringToIntegerConverter);
        return stringToIntegerConverter;
    }

    @Bean
    public ArgumentConverter<String> stringToDoubleConverter() {
        StringToDoubleConverter stringToDoubleConverter = new StringToDoubleConverter();
        ArgumentsComposerFactory.putConverter(stringToDoubleConverter);
        return stringToDoubleConverter;
    }
}
