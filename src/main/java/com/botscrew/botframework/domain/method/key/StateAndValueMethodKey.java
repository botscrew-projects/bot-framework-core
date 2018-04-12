package com.botscrew.botframework.domain.method.key;

import java.util.Objects;

public class StateAndValueMethodKey implements MethodKey {

    private final String state;
    private final String stringKey;

    public StateAndValueMethodKey(String state, String stringKey) {
        this.state = state;
        this.stringKey = stringKey;
    }

    public String getState() {
        return state;
    }

    public String getStringKey() {
        return stringKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateAndValueMethodKey that = (StateAndValueMethodKey) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(stringKey, that.stringKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, stringKey);
    }

    @Override
    public String toString() {
        return "StateAndValueMethodKey{" +
                "state='" + state + '\'' +
                ", stringKey='" + stringKey + '\'' +
                '}';
    }
}
