package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;

public class ReadContainer extends StateContainer {
    public ReadContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        super(stateHandlingMethodGroup);
    }

    public ReadContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(stateHandlingMethodGroup, stringParametersDetector);
    }
}
