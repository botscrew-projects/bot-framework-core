package com.botscrew.botframework.container.domain;

import com.botscrew.botframework.domain.ChatUser;

public class UserImpl implements ChatUser {

    private String state;

    public UserImpl(String state) {
        this.state = state;
    }

    @Override
    public String getState() {
        return state;
    }
}
