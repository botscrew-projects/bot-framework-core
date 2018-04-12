package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.domain.method.group.impl.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateAndValueMethodKey;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.domain.method.HandlingMethod;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class IntentHandlingMethodGroupTests {

    private IntentHandlingMethodGroup intentMethodGroup;

    @Before
    public void beforeEach() {
        intentMethodGroup = new IntentHandlingMethodGroup();
    }

    @Test
    public void shouldRegisterMethodsWithIntentAnnotation() throws Exception {
        intentMethodGroup.register(new ClassWithIntent());

        String state = "state";
        String intent = "default";
        StateAndValueMethodKey key = new StateAndValueMethodKey(state, intent);

        Optional<HandlingMethod> instanceMethod = intentMethodGroup.find(key);

        assertTrue(instanceMethod.isPresent());
    }

    private class ClassWithIntent {
        @Intent
        public void intent(ChatUser chatUser) {}
    }

    @Test
    public void shouldFirstlyLookForIntentMethod() throws Exception {
        intentMethodGroup.register(new ClassWithIntentAndStateMethod());

        StateAndValueMethodKey key = new StateAndValueMethodKey("STATE1", "INTENT1");
        Optional<HandlingMethod> instanceMethod = intentMethodGroup.find(key);

        assertTrue(instanceMethod.isPresent());

        Intent annotation = ((Method) ReflectionTestUtils.getField(instanceMethod.get(), "method")).getAnnotation(Intent.class);
        assertTrue(annotation.value().equals("INTENT1"));
    }

    private class ClassWithIntentAndStateMethod {

        @Intent(states = {"STATE1"})
        public void stateMethod() {}

        @Intent("INTENT1")
        public void intentMethod() {}
    }
}
