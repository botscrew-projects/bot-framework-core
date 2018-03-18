package com.botscrew.botframework.domain.param;

import java.util.Map;

public interface StringParametersDetector {

    public abstract String getValueWithoutParams(String withParams);
    public abstract Map<String, String> getParameters(String baseValue);
}
