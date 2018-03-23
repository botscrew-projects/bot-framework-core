package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Bot;

public interface Sender<B extends Bot, M extends Message> {

    void send(B bot, M message);
}
