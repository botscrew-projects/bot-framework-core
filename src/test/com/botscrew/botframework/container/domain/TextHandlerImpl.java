package com.botscrew.botframework.container.domain;

import com.botscrew.botframework.annotation.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextHandlerImpl {

    private final List<Consumer<Object[]>> consumers = new ArrayList<>();

    @Text
    public void consumeTextAndUser(@Text String text, UserImpl user) {
        Object[] args = new Object[2];
        args[0] = text;
        args[1] = user;

        consumers.forEach(c -> c.accept(args));
    }

    public void addCallback(Consumer<Object[]> consumer) {
        consumers.add(consumer);
    }


}
