package com.botscrew.botframework.integration;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.annotation.IntentProcessor;

@ChatEventsProcessor
@IntentProcessor
public class IntentAndChatEventsProcessor {

    @Intent
    public void intent() {
    }
}
