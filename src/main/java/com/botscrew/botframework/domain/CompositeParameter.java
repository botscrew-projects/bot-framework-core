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

package com.botscrew.botframework.domain;

import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.domain.argument.ArgumentType;

import java.lang.reflect.Parameter;

/**
 * Wrapper for {@link java.lang.reflect.Parameter} with additional information specific for Bot Framework
 */
public class CompositeParameter {
    private final ArgumentType type;
    private final Parameter originalParameter;
    private final boolean hasName;
    private final String name;

    public CompositeParameter(ArgumentType type, Parameter parameter) {
        this.type = type;
        this.originalParameter = parameter;
        this.hasName = detectIfNameIsPresent(parameter);
        this.name = detectName(parameter);
    }

    private static boolean detectIfNameIsPresent(Parameter parameter) {
        return parameter.isAnnotationPresent(Param.class);
    }

    private static String detectName(Parameter parameter) {
        if (parameter.isAnnotationPresent(Param.class)) {
            return parameter.getAnnotation(Param.class).value();
        }
        return parameter.getName();
    }

    public Class getOriginalType() {
        return this.originalParameter.getType();
    }

    public boolean hasName() {
        return hasName;
    }

    public String getName() {
        return name;
    }

    public ArgumentType getType() {
        return type;
    }

    public Parameter getOriginalParameter() {
        return originalParameter;
    }
}
