package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Read;
import com.botscrew.botframework.domain.method.group.impl.ReadHandlingMethodGroup;
import org.junit.Before;
import org.junit.Test;

public class ReadContainerTests {
    private ReadHandlingMethodGroup readHandlingMethodGroup;
    private ReadContainer readContainer;
    private boolean called;
    private Object[] params;


    @Before
    public void beforeEach() {
        readHandlingMethodGroup = new ReadHandlingMethodGroup();
        readContainer = new ReadContainer(readHandlingMethodGroup);
        called = false;
        params = new Object[0];
    }

    @Test
    public void shouldThrowExceptionIfNoMethodsAreRegistered() throws Exception {
        readHandlingMethodGroup.register(new SimpleReadHandler());

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
