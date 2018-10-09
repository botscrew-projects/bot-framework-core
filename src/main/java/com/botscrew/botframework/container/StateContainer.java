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

package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.exception.BotFrameworkException;

import java.util.Map;
import java.util.Optional;

/**
 * Container for handling methods based on user states.
 * Responsible for invoking appropriate method from registered.
 */
public abstract class StateContainer {
    private final StateHandlingMethodGroup stateHandlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public StateContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        this.stateHandlingMethodGroup = stateHandlingMethodGroup;
        this.stringParametersDetector = new SimpleStringParametersDetector();
    }

    public StateContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.stateHandlingMethodGroup = stateHandlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    /**
     * Entry point for specific events, will create empty argument kit
     * @param user User associated with an event
     * @see ArgumentKit
     * @see ChatUser
     */
    public void process(ChatUser user) {
        process(user, new SimpleArgumentKit());
    }

    /**
     * Entry point for specific events, accepts argument kit and adds additional parameters to it.
     * @param user User associated with an event
     * @param originalKit Set of additional available arguments for passing to target method
     */
    public void process(ChatUser user, ArgumentKit originalKit) {
        String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());
        StateMethodKey key = new StateMethodKey(stateWithoutParams);
        Optional<HandlingMethod> instanceMethod = stateHandlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new BotFrameworkException(createNoHandlingMethodError(stateWithoutParams));
        }
        Map<String, String> stateParameters = stringParametersDetector.getParameters(user.getState());

        originalKit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        originalKit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }

        instanceMethod.get().invoke(originalKit);
    }

    protected abstract String createNoHandlingMethodError(String state);
}
