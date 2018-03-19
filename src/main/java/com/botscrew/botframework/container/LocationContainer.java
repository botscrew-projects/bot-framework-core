package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ChatUser;
import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.LocationHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;

import java.util.Map;
import java.util.Optional;

public class LocationContainer {

    private final LocationHandlingMethodGroup locationHandlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public LocationContainer(LocationHandlingMethodGroup locationHandlingMethodGroup) {
        this.locationHandlingMethodGroup = locationHandlingMethodGroup;
        this.stringParametersDetector = new SimpleStringParametersDetector();
    }

    public LocationContainer(LocationHandlingMethodGroup locationHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.locationHandlingMethodGroup = locationHandlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    public void process(ChatUser user, ArgumentKit originalKit) {
        String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());
        StateMethodKey key = new StateMethodKey(stateWithoutParams);
        Optional<HandlingMethod> instanceMethod = locationHandlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method with @Location annotation found for state: " + stateWithoutParams);
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
