package com.botscrew.botframework.domain;

public class SimpleArgumentWrapper implements ArgumentWrapper {
    private final Object value;

    public SimpleArgumentWrapper(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
