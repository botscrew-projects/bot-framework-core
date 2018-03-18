package com.botscrew.botframework.domain.converter.impl;

import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.model.ArgumentType;

public class StringToByteConverter implements ArgumentConverter<String> {
    private static final ConverterKey KEY = ConverterKey.of(String.class, ArgumentType.PARAM_BYTE);

    @Override
    public ConverterKey getKey() {
        return KEY;
    }

    @Override
    public Object convert(String from, Class<?> originalParameter) {
        return Byte.parseByte(from);
    }
}
