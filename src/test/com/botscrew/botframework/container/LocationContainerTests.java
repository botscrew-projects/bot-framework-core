package com.botscrew.botframework.container;

import com.botscrew.botframework.container.domain.LocationHandlerImpl;
import com.botscrew.botframework.container.domain.TextHandlerImpl;
import com.botscrew.botframework.container.domain.UserImpl;
import com.botscrew.botframework.model.ChatUser;
import com.botscrew.botframework.model.GeoCoordinates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class LocationContainerTests {

    @Test
    public void locationShouldBeAvailableIfPassed() {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(0).equals(location);
    }

    @Test
    public void shouldPassChatUserImplToMethod() {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String state = "default";
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).getClass().equals(UserImpl.class);
        assert ((UserImpl) arguments.get(1)).getState().equals(state);
    }

    @Test
    public void shouldPassStringParametersToTargetMethod() throws Exception {
        String paramKey = "param";
        String paramValue = "value";
        String state = "stringParam?" + paramKey + "=" + paramValue;
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassIntParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Integer paramValue = 1;
        String state = "intParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassLongParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Long paramValue = 10L;
        String state = "longParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassByteParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Byte paramValue = 5;
        String state = "byteParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassShortParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Short paramValue = 3;
        String state = "shortParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassFloatParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Float paramValue = 4.83f;
        String state = "floatParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    @Test
    public void shouldPassDoubleParametersToTargetMethod() throws Exception {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        String paramKey = "param";
        Double paramValue = 224332.56;
        String state = "doubleParam?" + paramKey + "=" + paramValue;
        UserImpl user = new UserImpl(state);

        List<Object> arguments = executeLocation(location, user);

        assert arguments.get(1).equals(paramValue);
    }

    private List<Object> executeLocation(GeoCoordinates location, ChatUser user) {
        LocationContainer textContainer = new LocationContainer();
        LocationHandlerImpl handler = new LocationHandlerImpl();
        List<Object> arguments = new ArrayList<>();
        handler.addFollower(args -> arguments.addAll(Arrays.asList(args)));
        textContainer.register(handler);

        textContainer.processLocation(location, user);
        return arguments;
    }
}
