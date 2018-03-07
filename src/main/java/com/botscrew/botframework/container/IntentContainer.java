package com.botscrew.botframework.container;

import com.botscrew.botframework.model.ArgumentType;

import java.lang.reflect.Parameter;

public class IntentContainer extends AbstractContainer {


    @Override
    public void register(Object object) {

    }

    @Override
    protected ArgumentType getArgumentType(Parameter parameter) {
        return null;
    }
}
