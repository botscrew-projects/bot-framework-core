package com.botscrew.botframework.test;

import com.botscrew.botframework.annotation.Text;
import com.botscrew.botframework.model.ChatUser;

public class TestService {

    @Text
    public void processText(String text, ChatUser user, String param, Integer id) {
    }

}
