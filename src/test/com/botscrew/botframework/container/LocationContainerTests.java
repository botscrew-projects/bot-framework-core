package com.botscrew.botframework.container;

import com.botscrew.botframework.container.domain.GeoCoordinates;
import com.botscrew.botframework.container.domain.LocationHandlerImpl;
import com.botscrew.botframework.container.domain.UserImpl;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.group.LocationHandlingMethodGroup;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.user.ChatUser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LocationContainerTests {

    @Test
    public void locationShouldBeAvailableIfPassed() {
        GeoCoordinates location = new GeoCoordinates(1.0, 1.0);
        UserImpl user = new UserImpl("default");

        List<Object> arguments = executeLocation(location, user);

        assertEquals(location, arguments.get(0));
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
        LocationHandlingMethodGroup locationHandlingMethodGroup = new LocationHandlingMethodGroup();
        LocationContainer locationContainer = new LocationContainer(locationHandlingMethodGroup);
        LocationHandlerImpl handler = new LocationHandlerImpl();
        List<Object> arguments = new ArrayList<>();
        handler.addFollower(args -> arguments.addAll(Arrays.asList(args)));
        locationHandlingMethodGroup.register(handler);

        ArgumentKit kit = new SimpleArgumentKit();
        kit.put(ArgumentType.COORDINATES, new SimpleArgumentWrapper(location));
        locationContainer.process(user, kit);
        return arguments;
    }
}
