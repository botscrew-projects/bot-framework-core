package com.botscrew.botframework.integration;

import com.botscrew.botframework.configuration.BotFrameworkConfiguration;
import com.botscrew.botframework.container.IntentContainer;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.impl.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateAndValueMethodKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@EnableAsync
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
        Optional<HandlingMethod> handlingMethod = intentHandlingMethodGroup.find(new StateAndValueMethodKey("ALL_STATES", "ALL_INTENTS"));
        assert handlingMethod.isPresent();
    }
}
