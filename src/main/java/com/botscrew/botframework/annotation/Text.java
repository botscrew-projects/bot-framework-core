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

package com.botscrew.botframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that method need to be registered as `Text` event handler
 * Registered if exists inside class with {@link ChatEventsProcessor} annotation
 */
@Retention(RUNTIME)
@Target({METHOD, ElementType.PARAMETER})
public @interface Text {
    /**
     * Annotated method will be triggered if a user is in one of these states.
     * @see com.botscrew.botframework.domain.user.ChatUser
     */
    String[] states() default {};
}
