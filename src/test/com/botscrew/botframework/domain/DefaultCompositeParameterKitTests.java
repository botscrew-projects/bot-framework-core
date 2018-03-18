package com.botscrew.botframework.domain;

import com.botscrew.botframework.model.ArgumentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DefaultCompositeParameterKitTests {
    private SimpleArgumentKit kit;

    @Before
    public void beforeEach() {
        kit = new SimpleArgumentKit();
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldNotContain2ValuesWithSameName() {
        kit.put("name", new SimpleArgumentWrapper(1));
        kit.put("name", new SimpleArgumentWrapper(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotContain2ValuesWithSameTypeAndWithoutNames() {
        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(1));
        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(1));
    }

}
