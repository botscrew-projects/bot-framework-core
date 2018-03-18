package com.botscrew.botframework.domain;

import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.model.CompositeParameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArgumentsComposer {
    private final List<CompositeParameter> parameters;
    private final Map<ConverterKey, ArgumentConverter> converters;

    public ArgumentsComposer(List<CompositeParameter> parameters, Map<ConverterKey, ArgumentConverter> converters) {
        this.parameters = parameters;
        this.converters = converters;
    }

    public Object[] compose(ArgumentKit kit) {
        Object[] result = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            CompositeParameter param = parameters.get(i);
            Optional<ArgumentWrapper> wrapperOpt;
            if (param.hasName()) {
                wrapperOpt = kit.get(param.getName());
            }
            else {
                wrapperOpt = kit.get(param.getType());
            }

            if (wrapperOpt.isPresent()) {
                result[i] = convertIfNeededAndReturn(param, wrapperOpt.get());
            }
            else {
                result[i] = null;
            }
        }

        return result;
    }

    private Object convertIfNeededAndReturn(CompositeParameter parameter, ArgumentWrapper argumentWrapper) {
        Object value = argumentWrapper.getValue();
        if (isTheSameOrInherited(value.getClass(), parameter.getOriginalType())) {
            return value;
        }

        ArgumentConverter argumentConverter = converters.get(ConverterKey.of(value.getClass(), parameter.getType()));
        if (argumentConverter == null) {
            throw new ProcessorInnerException("Cannot convert type: " + value.getClass().toString() +
                    " to: " + parameter.getOriginalType().toString());
        }

        return argumentConverter.convert(value, parameter.getOriginalType());
    }

    private boolean isTheSameOrInherited(Class<?> argumentType, Class<?> parameterType) {
        return parameterType.equals(argumentType) || parameterType.isAssignableFrom(argumentType);
    }
}
