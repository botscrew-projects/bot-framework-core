package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;

public class TextContainer extends StateContainer {
    public TextContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        super(stateHandlingMethodGroup);
    }

    public TextContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(stateHandlingMethodGroup, stringParametersDetector);
    }
}
