package com.botscrew.botframework.domain.converter;

import com.botscrew.botframework.model.ArgumentType;

public class StringToDoubleConverter implements ArgumentConverter<String> {
    private static final ConverterKey KEY = ConverterKey.of(String.class, ArgumentType.PARAM_DOUBLE);

    @Override
    public ConverterKey getKey() {
        return KEY;
    }

    @Override
    public Object convert(String from, Class<?> originalParameter) {
        return Double.parseDouble(from);
    }
}
