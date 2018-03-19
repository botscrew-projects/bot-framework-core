package com.botscrew.botframework.domain.method.group;

public interface HandlingMethodGroup {
    void register(Object object);

    class Key {
        private Key() {}

        public static final String ALL_INTENTS = "ALL_INTENTS";
        public static final String ALL_STATES = "Key.ALL_STATES";
        public static final String ALL_POSTBACKS = "ALL_POSTBACKS";
    }
}
