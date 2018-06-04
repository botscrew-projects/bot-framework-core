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

package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.domain.method.HandlingMethod;

import java.util.Optional;

public interface HandlingMethodGroup<K> {
    void register(Object object);

    Optional<HandlingMethod> find(K key);

    class Key {
        public static final String ALL = "ALL";

        private Key() {
        }
    }
}
