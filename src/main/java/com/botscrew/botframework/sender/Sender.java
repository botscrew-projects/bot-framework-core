package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Chat;

public interface Sender<M extends Message> {

    void send(Chat chat, M m);
}
