package com.botscrew.botframework.container;

import com.botscrew.botframework.container.domain.TextHandlerImpl;
import com.botscrew.botframework.container.domain.UserImpl;
import com.botscrew.botframework.model.ChatUser;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;


@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextContainerTests {

    @Test
    public void textShouldBeAvailableIfParameterIsMappedWithText() {
        String text = "Just a text";
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executeText(text, user);

        assert arguments.get(0).getClass().equals(String.class);
        assert arguments.get(0).equals(text);
    }

    @Test
    public void textContainerShouldPassChatUserImplToMethod() {
        String text = "Just a text";
        String state = "default";
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText(text, user);

        assert arguments.get(1).getClass().equals(UserImpl.class);
        assert ((UserImpl) arguments.get(1)).getState().equals(state);
    }

    private List<Object> executeText(String text, ChatUser user) {
        TextContainer textContainer = new TextContainer();
        TextHandlerImpl textHandler = new TextHandlerImpl();
        List<Object> arguments = new ArrayList<>();
        textHandler.addCallback(args -> arguments.addAll(Arrays.asList(args)));
        textContainer.register(textHandler);

        textContainer.processText(text, user);
        return arguments;
    }
}
