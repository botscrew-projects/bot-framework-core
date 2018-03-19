package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ChatUser;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.IntentHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.BiMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;

import java.util.Map;
import java.util.Optional;

public class IntentContainer {

    private final IntentHandlingMethodGroup intentMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public IntentContainer(IntentHandlingMethodGroup intentMethodGroup) {
        stringParametersDetector = new SimpleStringParametersDetector();
        this.intentMethodGroup = intentMethodGroup;
    }

    public IntentContainer(IntentHandlingMethodGroup intentMethodGroup, StringParametersDetector stringParametersDetector) {
        this.intentMethodGroup = intentMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    public void process(ChatUser user, String intent, ArgumentKit originalKit) {
        if (originalKit == null) originalKit = new SimpleArgumentKit();

        BiMethodKey key = new BiMethodKey(user.getState(), intent);
        Optional<HandlingMethod> instanceMethod = intentMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method found for state: " + user.getState() + " and intent: " + intent);
        }
        Map<String, String> stateParameters = stringParametersDetector.getParameters(user.getState());

        originalKit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        originalKit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }

        instanceMethod.get().invoke(originalKit);
    }

}
