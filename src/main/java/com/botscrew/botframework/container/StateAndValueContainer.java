package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.kit.SimpleArgumentKit;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateAndValueMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Optional;

public abstract class StateAndValueContainer {
    private final HandlingMethodGroup handlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public StateAndValueContainer(HandlingMethodGroup handlingMethodGroup) {
        stringParametersDetector = new SimpleStringParametersDetector();
        this.handlingMethodGroup = handlingMethodGroup;
    }

    public StateAndValueContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.handlingMethodGroup = handlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    abstract void fillArgumentKit(ChatUser user, String value, ArgumentKit kit);

    public void process(ChatUser user, String value) {
        process(user, value, new SimpleArgumentKit());
    }

    public void process(ChatUser user, String value, ArgumentKit originalKit) {
        String valueWithoutParams = stringParametersDetector.getValueWithoutParams(value);

        StateAndValueMethodKey key = new StateAndValueMethodKey(user.getState(), valueWithoutParams);
        Optional<HandlingMethod> instanceMethod = handlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method found for state: " + user.getState() + " and value: " + value);
        }
        fillArgumentKit(user, value, originalKit);

        instanceMethod.get().invoke(originalKit);
    }

    public StringParametersDetector getStringParametersDetector() {
        return stringParametersDetector;
    }
}
