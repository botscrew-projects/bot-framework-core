/*
 * Copyright 2018 BotsCrew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.botscrew.botframework.domain.argument.composer;

import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.ArgumentWrapper;
import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.exception.ProcessorInnerException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Responsible for building list of arguments for invoking target method with
 *
 * @see ArgumentsComposerFactory
 */
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
            } else {
                wrapperOpt = kit.get(param.getType());
            }

            if (wrapperOpt.isPresent()) {
                result[i] = convertIfNeededAndReturn(param, wrapperOpt.get());
            } else {
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
