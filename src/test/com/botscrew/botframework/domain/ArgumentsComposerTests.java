package com.botscrew.botframework.domain;

import com.botscrew.botframework.domain.argument.composer.ArgumentsComposer;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.argument.ArgumentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentsComposerTests {


    @Test(expected = ProcessorInnerException.class)
    public void shouldThrowExceptionIfNoApplicableConverter() {
        Parameter param = TestClass.class.getMethods()[0].getParameters()[0];

        CompositeParameter parameter = new CompositeParameter(ArgumentType.PARAM_INTEGER, param);

        ArgumentsComposer composer = new ArgumentsComposer(Collections.singletonList(parameter), new HashMap<>());

        ArgumentKit kit = new SimpleArgumentKit();
        kit.put(ArgumentType.PARAM_INTEGER, new SimpleArgumentWrapper("2"));

        Object[] compose = composer.compose(kit);
    }

    private static class TestClass {

        public static void testMethod(Integer intParam) {
        }
    }
}
