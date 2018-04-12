package com.botscrew.botframework.domain.method.group;

import com.botscrew.botframework.domain.method.HandlingMethod;

import java.util.Optional;

public interface HandlingMethodGroup<K> {
    void register(Object object);

    Optional<HandlingMethod> find(K key);

    class Key {
        public static final String ALL = "ALL";

        private Key() {
        }
    }
}
