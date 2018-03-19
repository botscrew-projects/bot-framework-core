package com.botscrew.botframework.domain.converter.impl;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;

public class StringToIntegerConverter implements ArgumentConverter<String> {
    private static final ConverterKey KEY;

    static {
        KEY = ConverterKey.of(String.class, ArgumentType.PARAM_INTEGER);
    }

    @Override
    public ConverterKey getKey() {
        return KEY;
    }

    @Override
    public Object convert(String from, Class<?> originalParameter) {
        return Integer.parseInt(from);
    }
}
