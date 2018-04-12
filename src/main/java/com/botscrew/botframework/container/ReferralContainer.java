package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.argument.ArgumentType;
import com.botscrew.botframework.domain.argument.kit.ArgumentKit;
import com.botscrew.botframework.domain.argument.wrapper.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.group.HandlingMethodGroup;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.domain.user.ChatUser;

import java.util.Map;

public class ReferralContainer extends StateAndValueContainer {

    public ReferralContainer(HandlingMethodGroup handlingMethodGroup) {
        super(handlingMethodGroup);
    }

    public ReferralContainer(HandlingMethodGroup handlingMethodGroup, StringParametersDetector stringParametersDetector) {
        super(handlingMethodGroup, stringParametersDetector);
    }

    @Override
    void fillArgumentKit(ChatUser user, String value, ArgumentKit kit) {
        Map<String, String> stateParams = this.getStringParametersDetector().getParameters(user.getState());

        this.getStringParametersDetector().getParameters(value)
                .forEach((k, v) -> kit.put(k, new SimpleArgumentWrapper(v)));

        stateParams.forEach((k, v) -> kit.put(k, new SimpleArgumentWrapper(v)));

        kit.put(ArgumentType.REFERRAL, new SimpleArgumentWrapper(value));
        kit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParams));
        kit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
    }
}
