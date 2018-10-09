package com.botscrew.botframework.integration;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.annotation.IntentProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;

@ChatEventsProcessor
@IntentProcessor
@Async
public class IntentAndChatEventsProcessor {

    @Intent
    public void intent() {
    }
}
