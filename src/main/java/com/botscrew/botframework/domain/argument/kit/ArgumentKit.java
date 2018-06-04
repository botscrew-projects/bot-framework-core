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

import java.util.Optional;

/**
 * Describes the logic for saving method arguments by type or name and retrieving them
 * @see SimpleArgumentKit
 * @see ArgumentWrapper
 */
public interface ArgumentKit {

    void put(String name, ArgumentWrapper wrapper);

    void put(ArgumentType type, ArgumentWrapper wrapper);

    Optional<ArgumentWrapper> get(String name);

    Optional<ArgumentWrapper> get(ArgumentType type);
}
