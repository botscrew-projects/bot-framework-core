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

import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;

/**
 * Container implementation responsible for processing `delivery` events.
 */
public class DeliveryContainer extends StateContainer {
    public DeliveryContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        super(stateHandlingMethodGroup);
    }

    public DeliveryContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(stateHandlingMethodGroup, stringParametersDetector);
    }

    @Override
    protected String createNoHandlingMethodError(String state) {
        return "No delivery event handler for user with state: " + state;
    }
}
