package com.botscrew.botframework.container;

import com.botscrew.botframework.container.domain.TextHandlerImpl;
import com.botscrew.botframework.container.domain.UserImpl;
import com.botscrew.botframework.model.ChatUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
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

    @Test
    public void textContainerShouldPassStringParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        String paramValue = "value";
        String state = "stringParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassIntParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Integer paramValue = 1;
        String state = "intParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassLongParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Long paramValue = 10L;
        String state = "longParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassByteParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Byte paramValue = 5;
        String state = "byteParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassShortParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Short paramValue = 3;
        String state = "shortParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassFloatParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Float paramValue = 4.83f;
        String state = "floatParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassDoubleParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Double paramValue = 224332.56;
        String state = "doubleParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeText("text", user);

        assert arguments.get(1).equals(paramValue);
    }

    private List<Object> executeText(String text, ChatUser user) {
        TextContainer textContainer = new TextContainer();
        TextHandlerImpl textHandler = new TextHandlerImpl();
        List<Object> arguments = new ArrayList<>();
        textHandler.addFollower(args -> arguments.addAll(Arrays.asList(args)));
        textContainer.register(textHandler);

        textContainer.processText(text, user);
        return arguments;
    }
}