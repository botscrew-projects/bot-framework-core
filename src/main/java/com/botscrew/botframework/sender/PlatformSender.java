package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Chat;
import com.botscrew.botframework.domain.user.Platform;

import java.util.HashMap;
import java.util.Map;

public class PlatformSender implements Sender<Message> {
    private final Map<Platform, Sender> platformSenders;

    public PlatformSender() {
        this.platformSenders = new HashMap<>();
    }

    @Override
    public void send(Chat chat, Message message) {
        platformSenders.get(chat.getPlatform()).send(chat, message);
    }
}
