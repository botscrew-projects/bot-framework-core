package com.botscrew.botframework.model;

import java.util.Objects;

public class IntentMethodKey {

    private final String state;
    private final String intent;

    public IntentMethodKey(String state, String intent) {
        this.state = state;
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public String getIntent() {
        return intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntentMethodKey that = (IntentMethodKey) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(intent, that.intent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, intent);
    }
}
