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

package com.botscrew.botframework.domain.converter;

/**
 * Describes logic for converting some type of argument to another(f.e. `String - Integer`)
 * @see com.botscrew.botframework.domain.converter.impl.StringToIntegerConverter
 * @see com.botscrew.botframework.domain.converter.impl.StringToDoubleConverter
 * @param <F> type from which we will convert
 */
public interface ArgumentConverter<F> {

    ConverterKey getKey();

    Object convert(F from, Class<?> originalParameter);
}
