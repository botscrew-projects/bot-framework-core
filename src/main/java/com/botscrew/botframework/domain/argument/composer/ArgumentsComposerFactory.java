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
import com.botscrew.botframework.domain.converter.ArgumentConverter;
import com.botscrew.botframework.domain.converter.ConverterKey;
import com.botscrew.botframework.domain.converter.impl.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recommended way to create {@link ArgumentsComposer}, It adds registered converters to it.
 *
 */
public class ArgumentsComposerFactory {
    private static final Map<ConverterKey, ArgumentConverter> converters;

    static {
        converters = new HashMap<>();
        ArgumentsComposerFactory.putConverter(new UserConverter());
        ArgumentsComposerFactory.putConverter(new StringToIntegerConverter());
        ArgumentsComposerFactory.putConverter(new StringToLongConverter());
        ArgumentsComposerFactory.putConverter(new StringToDoubleConverter());
        ArgumentsComposerFactory.putConverter(new StringToFloatConverter());
        ArgumentsComposerFactory.putConverter(new StringToShortConverter());
        ArgumentsComposerFactory.putConverter(new StringToByteConverter());
    }

    private ArgumentsComposerFactory() {
    }

    /**
     * Adds converter to all {@link ArgumentsComposer} created from this Factory
     */
    public static void putConverter(ArgumentConverter converter) {
        converters.put(converter.getKey(), converter);
    }

    public static ArgumentsComposer create(List<CompositeParameter> parameters) {
        return new ArgumentsComposer(parameters, converters);
    }
}
