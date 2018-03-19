package com.botscrew.botframework.domain.argument.kit;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.wrapper.ArgumentWrapper;

import java.util.Optional;

public interface ArgumentKit {

    void put(String name, ArgumentWrapper wrapper);

    void put(ArgumentType type, ArgumentWrapper wrapper);

    Optional<ArgumentWrapper> get(String name);

    Optional<ArgumentWrapper> get(ArgumentType type);
}
