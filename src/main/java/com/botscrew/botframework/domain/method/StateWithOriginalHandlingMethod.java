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

package com.botscrew.botframework.domain.method;

import com.botscrew.botframework.annotation.Original;
import com.botscrew.botframework.annotation.StateParameters;
import com.botscrew.botframework.domain.CompositeParameter;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.user.ChatUser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Wrapper for {@link Method}
 * Defines method which can contain `Original` parameter {@link Original},
 * `StateParameters` {@link StateParameters} or basic types
 */
public class StateWithOriginalHandlingMethod extends HandlingMethod {
    public StateWithOriginalHandlingMethod(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected CompositeParameter createCompositeParameter(Parameter parameter) {
        if (ChatUser.class.isAssignableFrom(parameter.getType())) {
            return new CompositeParameter(ArgumentType.USER, parameter);
        }

        if (parameter.isAnnotationPresent(Original.class)) {
            return new CompositeParameter(ArgumentType.ORIGINAL_RESPONSE, parameter);
        }

        if (parameter.isAnnotationPresent(StateParameters.class)) {
            return new CompositeParameter(ArgumentType.STATE_PARAMETERS, parameter);
        }

        Optional<ArgumentType> baseTypeOpt = getBaseTypeArgumentByClass(parameter.getType());
        ArgumentType baseType = baseTypeOpt.orElse(ArgumentType.UNKNOWN);
        return new CompositeParameter(baseType, parameter);
    }
}
