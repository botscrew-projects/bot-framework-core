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
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Map;

/**
 * Container implementation responsible for processing `intent` events.
 */
public class IntentContainer extends StateAndValueContainer {

    public IntentContainer(HandlingMethodGroup handlingMethodGroup) {
        super(handlingMethodGroup);
    }

    public IntentContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(handlingMethodGroup, stringParametersDetector);
    }

    @Override
    void fillArgumentKit(ChatUser user, String value, ArgumentKit kit) {
        Map<String, String> stateParameters = this.getStringParametersDetector().getParameters(user.getState());
        Map<String, String> intentParameters = this.getStringParametersDetector().getParameters(value);

        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        kit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        kit.put(ArgumentType.INTENT, new SimpleArgumentWrapper(value));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
        for (Map.Entry<String, String> entry : intentParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
    }
}
