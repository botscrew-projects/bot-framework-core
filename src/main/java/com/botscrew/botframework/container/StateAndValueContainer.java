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

import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateAndValueMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.exception.BotFrameworkException;

import java.util.Optional;

/**
 * Container for handling methods based on user states and some additional key.
 * Responsible for invoking appropriate method from registered.
 */
public abstract class StateAndValueContainer {
    private final HandlingMethodGroup handlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public StateAndValueContainer(HandlingMethodGroup handlingMethodGroup) {
        stringParametersDetector = new SimpleStringParametersDetector();
        this.handlingMethodGroup = handlingMethodGroup;
    }

    public StateAndValueContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.handlingMethodGroup = handlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    abstract void fillArgumentKit(ChatUser user, String value, ArgumentKit kit);

    /**
     * Entry point for specific events
     * Accepts original value, will create empty argument kit
     * @param user User associated with an event
     * @param value Original event key value(with parameters)
     */
    public void process(ChatUser user, String value) {
        process(user, value, new SimpleArgumentKit());
    }

    /**
     * Entry point for specific events
     * @param user User associated with an event
     * @param value Original event key value(with parameters)
     * @param originalKit Set of additional available arguments for passing to target method
     */
    public void process(ChatUser user, String value, ArgumentKit originalKit) {
        String valueWithoutParams = stringParametersDetector.getValueWithoutParams(value);
        String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());

        StateAndValueMethodKey key = new StateAndValueMethodKey(stateWithoutParams, valueWithoutParams);
        Optional<HandlingMethod> instanceMethod = handlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new BotFrameworkException(createNoHandlingMethodError(stateWithoutParams, valueWithoutParams));
        }
        fillArgumentKit(user, value, originalKit);

        instanceMethod.get().invoke(originalKit);
    }

    protected abstract String createNoHandlingMethodError(String state, String value);

    public StringParametersDetector getStringParametersDetector() {
        return stringParametersDetector;
    }
}
