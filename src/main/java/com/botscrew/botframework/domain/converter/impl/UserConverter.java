package com.botscrew.botframework.domain.converter.impl;

import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;

public class UserConverter implements ArgumentConverter<ChatUser> {
    private static final ConverterKey KEY = ConverterKey.of(ChatUser.class, ArgumentType.USER);

    @Override
    public ConverterKey getKey() {
        return KEY;
    }

    @Override
    public Object convert(ChatUser from, Class<?> originalParameter) {
        return originalParameter.cast(from);
    }
}
