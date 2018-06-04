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

import com.botscrew.botframework.domain.argument.ArgumentType;

public class ConverterKey {
    private final Class fromType;
    private final ArgumentType toType;

    private ConverterKey(Class fromType, ArgumentType toType) {
        this.fromType = fromType;
        this.toType = toType;
    }

    public static ConverterKey of(Class fromType, ArgumentType toType) {
        return new ConverterKey(fromType, toType);
    }

    public Class getFromType() {
        return fromType;
    }

    public ArgumentType getToType() {
        return toType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConverterKey that = (ConverterKey) o;

        return (fromType != null ? fromType.equals(that.fromType) : that.fromType == null) && toType == that.toType;
    }

    @Override
    public int hashCode() {
        int result = fromType != null ? fromType.hashCode() : 0;
        result = 31 * result + (toType != null ? toType.hashCode() : 0);
        return result;
    }
}
