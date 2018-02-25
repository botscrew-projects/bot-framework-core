package com.botscrew.botframework.container;

import com.botscrew.botframework.container.domain.PostbackHandlerImpl;
import com.botscrew.botframework.container.domain.TextHandlerImpl;
import com.botscrew.botframework.container.domain.UserImpl;
import com.botscrew.botframework.model.ChatUser;
import javafx.geometry.Pos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class PostbackContainerTests {

    @Test
    public void postbackContainerShouldPassSimplePostbackToMethod() throws Exception {
        String postback = "postback";
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);
        
        assert arguments.get(0).equals(postback);
    }

    @Test
    public void postbackContainerShouldPassPostbackWithParametersToMethod() throws Exception {
        String postback = "postback?key=value?g=f";
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(0).equals(postback);
    }

    @Test
    public void postbackContainerShouldPassUserToMethod() throws Exception {
        String postback = "user";
        String state = "default";
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(0).equals(postback);

        Object userArg = arguments.get(1);

        assert userArg.getClass().equals(UserImpl.class);
        assert ((UserImpl) userArg).getState().equals(state);
    }

    @Test
    public void textContainerShouldPassStringParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        String paramValue = "value";
        String postback = "stringParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassIntParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Integer paramValue = 1;
        String postback = "intParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassLongParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Long paramValue = 10L;
        String postback = "longParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassByteParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Byte paramValue = 5;
        String postback = "byteParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassShortParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Short paramValue = 3;
        String postback = "shortParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassFloatParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Float paramValue = 4.83f;
        String postback = "floatParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void textContainerShouldPassDoubleParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        Double paramValue = 224332.56;
        String postback = "doubleParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executePostback(postback, user);

        assert arguments.get(1).equals(paramValue);
    }

    private List<Object> executePostback(String postback, ChatUser user) {
        PostbackContainer textContainer = new PostbackContainer();
        PostbackHandlerImpl handler = new PostbackHandlerImpl();
        List<Object> arguments = new ArrayList<>();
        handler.addFollower(args -> arguments.addAll(Arrays.asList(args)));
        textContainer.register(handler);

        textContainer.processPostback(postback, user);
        return arguments;
    }
}
