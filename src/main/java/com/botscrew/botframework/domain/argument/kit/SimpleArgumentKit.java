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

package com.botscrew.botframework.domain.argument.kit;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.wrapper.ArgumentWrapper;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation for {@link ArgumentKit}
 */
public class SimpleArgumentKit implements ArgumentKit {

    private final Map<String, ArgumentWrapper> namedArguments;
    private final Map<ArgumentType, ArgumentWrapper> typedArguments;

    public SimpleArgumentKit() {
        this.namedArguments = new HashMap<>();
        this.typedArguments = new EnumMap<>(ArgumentType.class);
    }

    @Override
    public void put(String name, ArgumentWrapper wrapper) {
        if (namedArguments.containsKey(name)) {
            throw new IllegalArgumentException("Kit already contains argument with name: " + name);
        }
        namedArguments.put(name, wrapper);
    }

    @Override
    public void put(ArgumentType type, ArgumentWrapper wrapper) {
        if (typedArguments.containsKey(type)) {
            throw new IllegalArgumentException("Kit already contains argument with type: " + type);
        }
        typedArguments.put(type, wrapper);
    }

    @Override
    public Optional<ArgumentWrapper> get(String name) {
        return Optional.ofNullable(namedArguments.get(name));
    }

    @Override
    public Optional<ArgumentWrapper> get(ArgumentType type) {
        return Optional.ofNullable(typedArguments.get(type));
    }
}
