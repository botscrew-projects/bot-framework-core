package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Map;

public class PostbackContainer extends StateAndValueContainer {

    public PostbackContainer(HandlingMethodGroup handlingMethodGroup) {
        super(handlingMethodGroup);
    }

    public PostbackContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(handlingMethodGroup, stringParametersDetector);
    }

    @Override
    void fillArgumentKit(ChatUser user, String value, ArgumentKit kit) {
        Map<String, String> stateParameters = this.getStringParametersDetector().getParameters(user.getState());
        Map<String, String> postbackParameters = this.getStringParametersDetector().getParameters(value);

        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        kit.put(ArgumentType.POSTBACK, new SimpleArgumentWrapper(value));
        kit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        kit.put(ArgumentType.POSTBACK_PARAMETERS, new SimpleArgumentWrapper(postbackParameters));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
        for (Map.Entry<String, String> entry : postbackParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
    }
}
