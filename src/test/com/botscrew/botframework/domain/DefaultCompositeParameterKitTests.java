package com.botscrew.botframework.domain;

import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ArgumentWrapper;
import org.junit.Before;
import org.junit.Test;

public class DefaultCompositeParameterKitTests {
    private DefaultArgumentKit kit;

    @Before
    public void beforeEach() {
        kit = new DefaultArgumentKit();
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldNotContain2ValuesWithSameName() {
        kit.put("name", new ArgumentWrapper());
        kit.put("name", new ArgumentWrapper());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotContain2ValuesWithSameTypeAndWithoutNames() {
        kit.put(ArgumentType.USER, new ArgumentWrapper());
        kit.put(ArgumentType.USER, new ArgumentWrapper());
    }

}
