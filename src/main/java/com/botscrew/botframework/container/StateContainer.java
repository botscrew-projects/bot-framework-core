package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.method.group.impl.TextHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Map;
import java.util.Optional;

public abstract class StateContainer {
    private final StateHandlingMethodGroup stateHandlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public StateContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        this.stateHandlingMethodGroup = stateHandlingMethodGroup;
        this.stringParametersDetector = new SimpleStringParametersDetector();
    }

    public StateContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.stateHandlingMethodGroup = stateHandlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    public void process(ChatUser user) {
        process(user, new SimpleArgumentKit());
    }

    public void process(ChatUser user, ArgumentKit originalKit) {
        String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());
        StateMethodKey key = new StateMethodKey(stateWithoutParams);
        Optional<HandlingMethod> instanceMethod = stateHandlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method found for state: " + stateWithoutParams);
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
