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

package com.botscrew.botframework.domain.argument;

/**
 * Custom types of parameters which we can define in the target method
 */
public enum ArgumentType {

    USER,
    STATE_PARAMETERS,
    POSTBACK,
    POSTBACK_PARAMETERS,
    TEXT,
    INTENT,
    COORDINATES,
    ORIGINAL_RESPONSE,
    COMPLEX_TYPE,
    UNKNOWN,

    PARAM_STRING,
    PARAM_INTEGER,
    PARAM_BYTE,
    PARAM_SHORT,
    PARAM_FLOAT,
    PARAM_DOUBLE,
    REFERRAL, PARAM_LONG
}
