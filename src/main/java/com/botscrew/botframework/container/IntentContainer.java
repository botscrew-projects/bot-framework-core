package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Map;

public class IntentContainer extends StateAndValueContainer {

    public IntentContainer(HandlingMethodGroup handlingMethodGroup) {
        super(handlingMethodGroup);
    }

    public IntentContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(handlingMethodGroup, stringParametersDetector);
    }

    @Override
    void fillArgumentKit(ChatUser user, String value, ArgumentKit kit) {
        Map<String, String> stateParameters = this.getStringParametersDetector().getParameters(user.getState());
        Map<String, String> intentParameters = this.getStringParametersDetector().getParameters(value);

        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        kit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        kit.put(ArgumentType.INTENT, new SimpleArgumentWrapper(value));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
        for (Map.Entry<String, String> entry : intentParameters.entrySet()) {
            kit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }
    }
}
