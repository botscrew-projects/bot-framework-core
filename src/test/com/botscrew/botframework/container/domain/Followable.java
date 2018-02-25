package com.botscrew.botframework.container.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Followable {

    private final List<Consumer<Object[]>> followers = new ArrayList<>();

    public List<Consumer<Object[]>> getFollowers() {
        return followers;
    }

    public void addFollower(Consumer<Object[]> consumer) {
        followers.add(consumer);
    }
}
