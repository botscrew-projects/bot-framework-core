package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Intent;
import com.botscrew.botframework.annotation.Read;
import com.botscrew.botframework.domain.method.group.impl.ReadHandlingMethodGroup;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReadContainerTests {
    private ReadHandlingMethodGroup readHandlingMethodGroup;
    private ReadContainer readContainer;
    private boolean called;


    @Before
    public void beforeEach() {
        readHandlingMethodGroup = new ReadHandlingMethodGroup();
        readContainer = new ReadContainer(readHandlingMethodGroup);
        called = false;
    }

    @Test
    public void shouldThrowExceptionIfNoMethodsAreRegistered() {
        Method method = Arrays.stream(SimpleReadHandler.class.getMethods())
                .filter(m -> m.getName().equals("read"))
                .findFirst().get();
        Read annotation = method.getAnnotation(Read.class);

        readHandlingMethodGroup.register(annotation, new SimpleReadHandler(), method);

        readContainer.process(() -> "default");

        assert called;
    }

    public class SimpleReadHandler {
        @Read
        public void read() {
            called = true;
        }
    }
}
