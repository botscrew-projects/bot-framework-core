package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;

public class EchoContainer extends StateContainer {
    public EchoContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        super(stateHandlingMethodGroup);
    }

    public EchoContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(stateHandlingMethodGroup, stringParametersDetector);
    }
}
