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

package com.botscrew.botframework.domain.param;

import java.util.Map;

/**
 * Describes logic for parsing String argument as value with parameters
 */
public interface StringParametersDetector {

    /**
     * @param withParams Original string value
     * @return Original value without params if present
     */
    String getValueWithoutParams(String withParams);

    /**
     *
     * @param baseValue Original string value
     * @return Map of parameters from the original value
     */
    Map<String, String> getParameters(String baseValue);
}
