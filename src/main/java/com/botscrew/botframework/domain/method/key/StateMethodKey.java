package com.botscrew.botframework.domain.method.key;

import java.util.Objects;

public class StateMethodKey implements MethodKey {
    private final String state;

    public StateMethodKey(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateMethodKey that = (StateMethodKey) o;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

    @Override
    public String toString() {
        return "StateMethodKey{" +
                "state='" + state + '\'' +
                '}';
    }
}
