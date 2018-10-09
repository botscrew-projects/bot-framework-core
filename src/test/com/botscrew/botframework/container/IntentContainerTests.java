package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.*;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.method.group.impl.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.user.ChatUser;
import com.botscrew.botframework.exception.BotFrameworkException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
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
    public void shouldThrowExceptionIfNoMethodsAreRegistered() {
        ChatUser user = () -> "default";
        String intent = "intent";

        intentContainer.process(user, intent, new SimpleArgumentKit());
    }

    @Test
    public void shouldProcessDefaultRegisteredMethod() {
        Method method = Arrays.stream(DefaultIntentHandler.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new DefaultIntentHandler(), method);
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
    public void shouldTransferUserToMethod() {
        Method method = Arrays.stream(DefaultIntentHandlerWithUser.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new DefaultIntentHandlerWithUser(), method);

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
    public void shouldPassStateParamsToMethod() {
        Method method = Arrays.stream(IntentWithStateParams.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithStateParams(), method);


        intentContainer.process(() -> "default?key=value", "intent");

        assertEquals("value", ((Map<String, String>)params[0]).get("key"));
    }

    public class IntentWithStateParams {
        @Intent
        public void defaultMethod(@StateParameters Map<String, String> p) {
            called = true;
            params = new Object[1];
            params[0] = p;
        }
    }

    @Test
    public void shouldPassSpecificStringStateParamToMethod() {
        Method method = Arrays.stream(IntentWithStringParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithStringParam(), method);

        intentContainer.process(() -> "default?param=value", "intent");

        assertEquals("value", params[0]);
    }

    public class IntentWithStringParam {
        @Intent
        public void defaultMethod(@Param("param") String param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassIntStateParamToMethod() {
        Method method = Arrays.stream(IntentWithIntParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithIntParam(), method);

        intentContainer.process(() -> "default?param=1", "intent");

        assertEquals(1, params[0]);
    }

    public class IntentWithIntParam {
        @Intent
        public void defaultMethod(@Param("param") Integer param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassLongStateParamToMethod() {
        Method method = Arrays.stream(IntentWithLongParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithLongParam(), method);

        intentContainer.process(() -> "default?param=1", "intent");

        assertEquals(1L, params[0]);
    }

    public class IntentWithLongParam {
        @Intent
        public void defaultMethod(@Param("param") Long param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassByteStateParamToMethod() {
        Method method = Arrays.stream(IntentWithByteParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithByteParam(), method);

        intentContainer.process(() -> "default?param=1", "intent");

        byte expected = 1;
        assertEquals(expected, params[0]);
    }

    public class IntentWithByteParam {
        @Intent
        public void defaultMethod(@Param("param") Byte param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassShortStateParamToMethod() {
        Method method = Arrays.stream(IntentWithShortParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithShortParam(), method);

        intentContainer.process(() -> "default?param=1", "intent");

        short expected = 1;
        assertEquals(expected, params[0]);
    }

    public class IntentWithShortParam {
        @Intent
        public void defaultMethod(@Param("param") Short param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassFloatStateParamToMethod() {
        Method method = Arrays.stream(IntentWithFloatParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithFloatParam(), method);

        intentContainer.process(() -> "default?param=1.0", "intent", new SimpleArgumentKit());

        float expected = 1.0f;
        assertEquals(expected, params[0]);
    }

    public class IntentWithFloatParam {
        @Intent
        public void defaultMethod(@Param("param") Float param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test
    public void shouldPassDoubleStateParamToMethod() {
        Method method = Arrays.stream(IntentWithDoubleParam.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentWithDoubleParam(), method);
        intentContainer.process(() -> "default?param=1.0", "intent", new SimpleArgumentKit());

        double expected = 1.0d;
        assertEquals(expected, params[0]);
    }

    public class IntentWithDoubleParam {
        @Intent
        public void defaultMethod(@Param("param") Double param) {
            called = true;
            params = new Object[1];
            params[0] = param;
        }
    }

    @Test(expected = BotFrameworkException.class)
    public void shouldThrowExceptionIfMethodContainsAFewParamsWithTheSameName() {
        Method method = Arrays.stream(IntentProcessorWithTheSameParamNames.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentProcessorWithTheSameParamNames(), method);
    }

    public class IntentProcessorWithTheSameParamNames {
        @Intent
        public void defaultMethod(@Param("one") String one, @Param("one") String two) {}
    }

    @Test(expected = BotFrameworkException.class)
    public void shouldThrowExceptionIfMethodContainsAFewParamsWithTheSameTypeAndWithoutName() {
        Method method = Arrays.stream(IntentProcessorWithTheSameParamTypes.class.getMethods())
                .filter(m -> m.getName().equals("defaultMethod"))
                .findFirst().get();
        Intent annotation = method.getAnnotation(Intent.class);

        intentMethodGroup.register(annotation, new IntentProcessorWithTheSameParamTypes(), method);
    }

    public class IntentProcessorWithTheSameParamTypes {
        @Intent
        public void defaultMethod(String one, String two) {}
    }
}
