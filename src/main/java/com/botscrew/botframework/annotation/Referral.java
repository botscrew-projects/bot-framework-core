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
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that method need to be registered as `Referral` event handler
 * Registered if exists inside class with {@link ChatEventsProcessor} annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Referral {

    /**
     * @return original referral value
     * Annotated method will be triggered if actual value is equal to it and there is no more specific method.
     */
    String value() default "";

    /**
     * Annotated method will be triggered if a user is in one of these states and there is no more specific method.
     * @see com.botscrew.botframework.domain.user.ChatUser
     * @return List of possible user states
     */
    String[] states() default {};
}
