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

package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Referral;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.ReferralHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateAndValueHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Defines group of methods which is responsible for handling `Referral` event
 */
public class ReferralHandlingMethodGroup extends StateAndValueHandlingMethodGroup {

    public ReferralHandlingMethodGroup() {
        super(Referral.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Referral) annotation).states());
    }

    @Override
    public String getValue(Annotation annotation) {
        return ((Referral) annotation).value();
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new ReferralHandlingMethod(object, method);
    }
}
