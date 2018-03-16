package com.botscrew.botframework.container.domain;

import com.botscrew.botframework.annotation.Location;
import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.model.GeoCoordinates;

public class LocationHandlerImpl extends Followable {

    @Location
    public void consumeTextAndUser(GeoCoordinates coordinates, UserImpl user) {
        Object[] args = new Object[2];
        args[0] = coordinates;
        args[1] = user;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"stringParam"})
    public void consumeTextAndStringParam(UserImpl user, @Param("param") String param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"intParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Integer param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"longParam"})
    public void consumeTextAndLongParam(UserImpl user, @Param("param") Long param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"byteParam"})
    public void consumeTextAndByteParam(UserImpl user, @Param("param") Byte param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"shortParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Short param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }


    @Location(states = {"floatParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Float param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Location(states = {"doubleParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Double param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }
}
