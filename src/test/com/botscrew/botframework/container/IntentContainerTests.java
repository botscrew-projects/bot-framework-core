package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.*;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.method.group.impl.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.exception.ProcessorInnerException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class IntentContainerTests {

    private IntentHandlingMethodGroup intentMethodGroup;
    private IntentContainer intentContainer;
    private boolean called;
    private Object[] params;

    @Before
    public void beforeEach() {
        intentMethodGroup = new IntentHandlingMethodGroup();
        intentContainer = new IntentContainer(intentMethodGroup);
        called = false;
        params = new Object[0];
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoMethodsAreRegistered() throws Exception {
        ChatUser user = () -> "default";
        String intent = "intent";

        intentContainer.process(user, intent, new SimpleArgumentKit());
    }

    @Test
    public void shouldProcessDefaultRegisteredMethod() throws Exception {
        intentMethodGroup.register(new DefaultIntentHandler());

        intentContainer.process(() -> "default", "intent", new SimpleArgumentKit());

        assertTrue(called);
    }

    public class DefaultIntentHandler {

        @Intent
        public void defaultMethod() {
            called = true;
        }

    }

    @Test
    public void shouldTransferUserToMethod() throws Exception {
        intentMethodGroup.register(new DefaultIntentHandlerWithUser());

        MyUser user = new MyUser();
        intentContainer.process(user, "intent", new SimpleArgumentKit());

        assertTrue(called);
        assertEquals(user, params[0]);
    }

    public class DefaultIntentHandlerWithUser {

        @Intent
        public void defaultMethod(MyUser user) {
            params = new Object[1];
            params[0] = user;
            called = true;
        }

    }

    public class MyUser implements ChatUser {

        @Override
        public String getState() {
            return "default";
        }
    }

    @Test
    public void shouldPassStateParamsToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithStateParams());


        intentContainer.process(() -> "default?key=value", "intent");

        assertEquals("value", ((Map<String, String>)params[0]).get("key"));
    }

    public class IntentWithStateParams {
        @Intent
        public void intentWithParams(@StateParameters Map<String, String> p) {
            called = true;
            params = new Object[1];
            params[0] = p;
        }
    }

    @Test
    public void shouldPassSpecificStringStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithStringParam());

        intentContainer.process(() -> "default?param=value", "intent");

        assertEquals("value", params[0]);
    }

    public class IntentWithStringParam {
        @Intent
        public void intentWithParams(@Param("param") String param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassIntStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithIntParam());

        intentContainer.process(() -> "default?param=1", "intent");

        assertEquals(1, params[0]);
    }

    public class IntentWithIntParam {
        @Intent
        public void intentWithParams(@Param("param") Integer param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassLongStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithLongParam());

        intentContainer.process(() -> "default?param=1", "intent");

        assertEquals(1L, params[0]);
    }

    public class IntentWithLongParam {
        @Intent
        public void intentWithParams(@Param("param") Long param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassByteStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithByteParam());

        intentContainer.process(() -> "default?param=1", "intent");

        byte expected = 1;
        assertEquals(expected, params[0]);
    }

    public class IntentWithByteParam {
        @Intent
        public void intentWithParams(@Param("param") Byte param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassShortStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithShortParam());

        intentContainer.process(() -> "default?param=1", "intent");

        short expected = 1;
        assertEquals(expected, params[0]);
    }

    public class IntentWithShortParam {
        @Intent
        public void intentWithParams(@Param("param") Short param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassFloatStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithFloatParam());

        intentContainer.process(() -> "default?param=1.0", "intent", new SimpleArgumentKit());

        float expected = 1.0f;
        assertEquals(expected, params[0]);
    }

    public class IntentWithFloatParam {
        @Intent
        public void intentWithParams(@Param("param") Float param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassDoubleStateParamToMethod() throws Exception {
        intentMethodGroup.register(new IntentWithDoubleParam());
        intentContainer.process(() -> "default?param=1.0", "intent", new SimpleArgumentKit());

        double expected = 1.0d;
        assertEquals(expected, params[0]);
    }

    public class IntentWithDoubleParam {
        @Intent
        public void intentWithParams(@Param("param") Double param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test(expected = ProcessorInnerException.class)
    public void shouldThrowExceptionIfMethodContainsAFewParamsWithTheSameName() {
        intentMethodGroup.register(new IntentProcessorWithTheSameParamNames());
    }

    public class IntentProcessorWithTheSameParamNames {
        @Intent
        public void intent(@Param("one") String one, @Param("one") String two) {}
    }

    @Test(expected = ProcessorInnerException.class)
    public void shouldThrowExceptionIfMethodContainsAFewParamsWithTheSameTypeAndWithoutName() {
        intentMethodGroup.register(new IntentProcessorWithTheSameParamTypes());
    }

    public class IntentProcessorWithTheSameParamTypes {
        @Intent
        public void intent(String one, String two) {}
    }
}
