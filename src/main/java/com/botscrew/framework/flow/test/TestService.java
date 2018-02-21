package com.botscrew.framework.flow.test;

import com.botscrew.framework.flow.annotation.Text;
import com.botscrew.framework.flow.model.ChatUser;

public class TestService {

    @Text
    public void processText(String text, ChatUser user, String param, Integer id) {
    }

}
