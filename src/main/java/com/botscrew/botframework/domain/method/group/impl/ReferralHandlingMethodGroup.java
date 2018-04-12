package com.botscrew.botframework.domain.method.group.impl;

import com.botscrew.botframework.annotation.Referral;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.IntentHandlingMethod;
import com.botscrew.botframework.domain.method.ReferralHandlingMethod;
import com.botscrew.botframework.domain.method.group.StateAndValueHandlingMethodGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ReferralHandlingMethodGroup extends StateAndValueHandlingMethodGroup {

    public ReferralHandlingMethodGroup() {
        super(Referral.class);
    }

    @Override
    public List<String> getStates(Annotation annotation) {
        return Arrays.asList(((Referral) annotation).states());
    }

    @Override
    public String getValue(Annotation annotation) {
        return ((Referral) annotation).value();
    }

    @Override
    public HandlingMethod createHandlingMethod(Object object, Method method) {
        return new ReferralHandlingMethod(object, method);
    }
}
