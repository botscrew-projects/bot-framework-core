package com.botscrew.botframework.domain.method.group;

public interface HandlingMethodGroup {
    String ALL_INTENTS = "ALL_INTENTS";
    String ALL_STATES = "ALL_STATES";
    String ALL_POSTBACKS = "ALL_POSTBACKS";

    void register(Object object);
}
