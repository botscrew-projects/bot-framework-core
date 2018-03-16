package com.botscrew.botframework.domain.converter;

public interface ArgumentConverter<F> {

    ConverterKey getKey();
    Object convert(F from, Class<?> originalParameter);
}
