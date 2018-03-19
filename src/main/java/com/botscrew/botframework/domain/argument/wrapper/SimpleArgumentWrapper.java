package com.botscrew.botframework.domain.argument.wrapper;

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
