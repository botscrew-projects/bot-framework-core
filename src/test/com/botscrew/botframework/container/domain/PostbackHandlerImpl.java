package com.botscrew.botframework.container.domain;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.annotation.Postback;

@ChatEventsProcessor
public class PostbackHandlerImpl extends Followable {
    
    @Postback("postback")
    public void process(@Postback String postback) {
        Object[] args = new Object[1];
        args[0] = postback;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("user")
    public void process(@Postback String postback, UserImpl user) {
        Object[] args = new Object[2];
        args[0] = postback;
        args[1] = user;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("stringParam")
    public void consumeStringParam(UserImpl user, @Param("param") String param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("intParam")
    public void consumeIntParam(UserImpl user, @Param("param") Integer param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("longParam")
    public void consumeLongParam(UserImpl user, @Param("param") Long param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("byteParam")
    public void consumeByteParam(UserImpl user, @Param("param") Byte param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("shortParam")
    public void consumeShortParam(UserImpl user, @Param("param") Short param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }


    @Postback("floatParam")
    public void consumeFloatParam(UserImpl user, @Param("param") Float param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }

    @Postback("doubleParam")
    public void consumeDoubleParam(UserImpl user, @Param("param") Double param) {
        Object[] args = new Object[2];
        args[0] = user;
        args[1] = param;

        getFollowers().forEach(c -> c.accept(args));
    }
}
