package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.SimpleArgumentKit;
import com.botscrew.botframework.domain.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.TextHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.StateMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;

import java.util.Map;
import java.util.Optional;

public class TextContainer extends AbstractContainer {
    private final TextHandlingMethodGroup textHandlingMethodGroup;
    private final StringParametersDetector stringParametersDetector;

    public TextContainer(TextHandlingMethodGroup textHandlingMethodGroup) {
        this.textHandlingMethodGroup = textHandlingMethodGroup;
        this.stringParametersDetector = new SimpleStringParametersDetector();
    }

    public TextContainer(TextHandlingMethodGroup textHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        this.textHandlingMethodGroup = textHandlingMethodGroup;
        this.stringParametersDetector = stringParametersDetector;
    }

    public void process(ChatUser user, ArgumentKit originalKit) {
        if (originalKit == null) originalKit = new SimpleArgumentKit();

        String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());
        StateMethodKey key = new StateMethodKey(stateWithoutParams);
        Optional<HandlingMethod> instanceMethod = textHandlingMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method with @Text annotation found for state: " + stateWithoutParams);
        }
        Map<String, String> stateParameters = stringParametersDetector.getParameters(user.getState());

        originalKit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        originalKit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
        for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
            originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }

        tryInvokeMethod(instanceMethod.get(), originalKit);
    }
}
