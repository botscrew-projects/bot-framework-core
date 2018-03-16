package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.model.ChatUser;
import com.botscrew.botframework.model.IntentInstanceMethod;
import com.botscrew.botframework.model.IntentMethodKey;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class IntentMethodGroupTests {

    private IntentMethodGroup intentMethodGroup;

    @Before
    public void beforeEach() {
        intentMethodGroup = new IntentMethodGroup();
    }

    @Test
    public void shouldRegisterMethodsWithIntentAnnotation() throws Exception {
        intentMethodGroup.register(new ClassWithIntent());

        String state = "state";
        String intent = "default";
        IntentMethodKey key = new IntentMethodKey(state, intent);

        Optional<IntentInstanceMethod> instanceMethod = intentMethodGroup.find(key);

        assertTrue(instanceMethod.isPresent());
    }

    private class ClassWithIntent {
        @Intent
        public void intent(ChatUser chatUser) {}
    }

    @Test
    public void shouldFirstlyLookForIntentMethod() throws Exception {
        intentMethodGroup.register(new ClassWithIntentAndStateMethod());

        IntentMethodKey key = new IntentMethodKey("STATE1", "INTENT1");
        Optional<IntentInstanceMethod> instanceMethod = intentMethodGroup.find(key);

        assertTrue(instanceMethod.isPresent());

        Intent annotation = instanceMethod.get().getMethod().getAnnotation(Intent.class);
        assertTrue(annotation.name().equals("INTENT1"));
    }

    private class ClassWithIntentAndStateMethod {

        @Intent(states = {"STATE1"})
        public void stateMethod() {}

        @Intent(name = "INTENT1")
        public void intentMethod() {}
    }
}
