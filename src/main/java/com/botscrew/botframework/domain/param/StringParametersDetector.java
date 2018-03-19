package com.botscrew.botframework.domain.param;

import java.util.Map;

public interface StringParametersDetector {

    String getValueWithoutParams(String withParams);

    Map<String, String> getParameters(String baseValue);
}
