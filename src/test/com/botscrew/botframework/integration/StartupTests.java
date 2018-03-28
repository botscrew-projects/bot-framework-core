package com.botscrew.botframework.integration;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.annotation.IntentProcessor;
import com.botscrew.botframework.configuration.BotFrameworkConfiguration;
import com.botscrew.botframework.container.IntentContainer;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.BiMethodKey;
import com.botscrew.botframework.domain.user.ChatUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BotFrameworkConfiguration.class, IntentAndChatEventsProcessor.class})
public class StartupTests {

    @Autowired
    private IntentContainer intentContainer;
    @Autowired
    private IntentHandlingMethodGroup intentHandlingMethodGroup;

    @Test
    public void shouldNotRegisterOneMethod2TimesIfClassHasBothChatEventsProcessorAndIntentProcessorAnnotations() {
        intentContainer.process(() -> "state", "intent");
        Optional<HandlingMethod> handlingMethod = intentHandlingMethodGroup.find(new BiMethodKey("ALL_STATES", "ALL_INTENTS"));

        assert handlingMethod.isPresent();
    }
}
