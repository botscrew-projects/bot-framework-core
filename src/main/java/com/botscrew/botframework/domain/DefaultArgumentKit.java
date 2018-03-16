package com.botscrew.botframework.domain;

import com.botscrew.botframework.model.ArgumentType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultArgumentKit implements ArgumentKit {

    private final Map<String, ArgumentWrapper> namedArguments;
    private final Map<ArgumentType, ArgumentWrapper> typedArguments;

    public DefaultArgumentKit() {
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
