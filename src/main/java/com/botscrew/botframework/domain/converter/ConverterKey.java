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
