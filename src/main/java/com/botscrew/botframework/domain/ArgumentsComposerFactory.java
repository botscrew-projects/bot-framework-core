package com.botscrew.botframework.domain;

import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.model.CompositeParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentsComposerFactory {
    private static final Map<ConverterKey, ArgumentConverter> converters;
    static {
        converters = new HashMap<>();
    }

    private ArgumentsComposerFactory() {}

    public static void putConverter(ArgumentConverter converter) {
        converters.put(converter.getKey(), converter);
    }

    public static ArgumentsComposer create(List<CompositeParameter> parameters) {
        return new ArgumentsComposer(parameters, converters);
    }
}
