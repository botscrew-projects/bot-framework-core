package com.botscrew.botframework.domain;

import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ArgumentWrapper;

import java.util.Optional;

public interface ArgumentKit {

    void put(String name, ArgumentWrapper wrapper);

    void put(ArgumentType type, ArgumentWrapper wrapper);

    Optional<ArgumentWrapper> get(String name);
    Optional<ArgumentWrapper> get(ArgumentType type);

}
