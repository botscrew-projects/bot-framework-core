package com.botscrew.framework.flow.container.converter;

import java.lang.reflect.Parameter;

public interface ArgumentConverter<F, T> {
    T convert(Parameter parameter, F f);
}
