package com.botscrew.botframework.test;

import com.botscrew.botframework.model.ChatUser;

public class MyUser implements ChatUser {

    private String state = "default";

    @Override
    public String getState() {
        return state;
    }

    public MyUser(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "state='" + state + '\'' +
                '}';
    }
}
