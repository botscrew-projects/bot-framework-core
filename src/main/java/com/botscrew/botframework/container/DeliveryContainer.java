package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.method.group.StateHandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;

public class DeliveryContainer extends StateContainer {
    public DeliveryContainer(StateHandlingMethodGroup stateHandlingMethodGroup) {
        super(stateHandlingMethodGroup);
    }

    public DeliveryContainer(StateHandlingMethodGroup stateHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(stateHandlingMethodGroup, stringParametersDetector);
    }
}
