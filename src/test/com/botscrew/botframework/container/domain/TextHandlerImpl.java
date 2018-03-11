package com.botscrew.botframework.container.domain;

import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.annotation.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextHandlerImpl extends Followable {

    @Text
    public void consumeTextAndUser(@Text String text, UserImpl user) {
        Object[] args = new Object[2];
        args[0] = text;
        args[1] = user;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"stringParam"})
    public void consumeTextAndStringParam(UserImpl user, @Param("param") String param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"intParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Integer param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"longParam"})
    public void consumeTextAndLongParam(UserImpl user, @Param("param") Long param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"byteParam"})
    public void consumeTextAndByteParam(UserImpl user, @Param("param") Byte param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"shortParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Short param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }


    @Text(states = {"floatParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Float param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Text(states = {"doubleParam"})
    public void consumeTextAndIntParam(UserImpl user, @Param("param") Double param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

}
