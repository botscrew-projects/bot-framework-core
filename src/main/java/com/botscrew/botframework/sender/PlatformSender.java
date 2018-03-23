package com.botscrew.botframework.sender;

import com.botscrew.botframework.domain.user.Bot;
import com.botscrew.botframework.domain.user.Platform;

import java.util.HashMap;
import java.util.Map;

public class PlatformSender implements Sender<Bot, Message> {
    private final Map<Platform, Sender> platformSenders;

    public PlatformSender() {
        this.platformSenders = new HashMap<>();
    }

    public void addSender(Platform platform, Sender sender) {
        platformSenders.put(platform, sender);
    }

    @Override
    public void send(Bot bot, Message message) {
        platformSenders.get(bot.getPlatform()).send(bot, message);
    }
}
