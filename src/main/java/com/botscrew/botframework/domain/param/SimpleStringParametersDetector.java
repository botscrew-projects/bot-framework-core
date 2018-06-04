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

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for {@link StringParametersDetector}
 * This implementation defines argument as value and array of parameters parted with `?`(parameters also parted with `?`)
 * Each parameter key and value parted with `=`
 *
 * It looks like that: SIMPLE_NAME?first_key=FIRST_VALUE?second_key=SECOND_VALUE
 * And with will parted as:
 * Value without params: SIMPLE_NAME
 * Parameters: {"first_key" = "FIRST_VALUE", "second_key" = "SECOND_VALUE"}
 */
public class SimpleStringParametersDetector implements StringParametersDetector {
    private final String equalitySign;
    private final String spliterator;

    public SimpleStringParametersDetector() {
        equalitySign = "=";
        spliterator = "?";
    }

    public SimpleStringParametersDetector(String equalitySign, String spliterator) {
        this.equalitySign = equalitySign;
        this.spliterator = spliterator;
    }

    @Override
    public String getValueWithoutParams(final String withParams) {
        if (withParams.contains(spliterator)) {
            return withParams.substring(0, withParams.indexOf(spliterator));
        }
        return withParams;
    }

    @Override
    public Map<String, String> getParameters(final String baseValue) {
        Map<String, String> parameters = new HashMap<>();

        if (baseValue.contains(spliterator)) {

            String parametersString = baseValue.substring(baseValue.indexOf(spliterator) + spliterator.length());
            while (true) {
                if (parametersString.contains(equalitySign)) {
                    String key = findKey(parametersString);
                    String value = findValue(parametersString);
                    if (!key.isEmpty() && !value.isEmpty()) {
                        parameters.put(key, value);
                    }
                    parametersString = nextPatameter(parametersString);
                } else {
                    break;
                }
            }
        }
        return parameters;
    }

    private String nextPatameter(String parametersString) {
        if (!parametersString.contains(spliterator)) {
            return "";
        }
        return parametersString.substring(parametersString.indexOf(spliterator) + spliterator.length());
    }

    private String findKey(String parametersString) {
        return parametersString.substring(0, parametersString.indexOf(equalitySign));
    }

    private String findValue(String parametersString) {
        if (parametersString.indexOf(equalitySign) + equalitySign.length() == parametersString.length()) {
            return "";
        }

        int spliteratorIndex = parametersString.indexOf(spliterator);
        int endIndex = spliteratorIndex < 0 ? parametersString.length() : spliteratorIndex;
        return parametersString.substring(parametersString.indexOf(equalitySign) + 1, endIndex);
    }
}
