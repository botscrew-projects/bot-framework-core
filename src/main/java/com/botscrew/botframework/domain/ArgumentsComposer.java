package com.botscrew.botframework.domain;

import com.botscrew.botframework.model.CompositeParameter;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

public class ArgumentsComposer {
    private final List<CompositeParameter> parameters;

    public ArgumentsComposer(List<CompositeParameter> parameters) {
        this.parameters = parameters;
    }

    Object[] compose(ArgumentKit kit) {
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
                result[i] = convertIfNeededAndReturn(wrapperOpt.get());
            }
            else {
                result[i] = null;
            }
        }
    }

    private Object convertIfNeededAndReturn(Parameter parameter, ArgumentWrapper argumentWrapper) {
        Object value = argumentWrapper.getValue();
        if (parameter.getType().equals(value.getClass())) return value;


    }
}
