package com.botscrew.botframework.util;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtils {

	private static final String EQUALITY_SIGN = "=";

	private ParametersUtils() {
	}

	public static String getValueWithoutParams(final String withParams, final String spliterator) {
		if (withParams.contains(spliterator)) {
			return withParams.substring(0, withParams.indexOf(spliterator));
		}
		return withParams;
	}

	public static Map<String, String> getParameters(final String baseValue, final String spliterator) {
		Map<String, String> parameters = new HashMap<>();

		if (baseValue.contains(spliterator)) {

			String parametersString = baseValue.substring(baseValue.indexOf(spliterator) + spliterator.length());
			while (true) {
				if (parametersString.contains(EQUALITY_SIGN)) {
					String key = findKey(parametersString);
					String value = findValue(parametersString, spliterator);
					if (!key.isEmpty() && !value.isEmpty()) {
						parameters.put(key, value);
					}
					parametersString = nextPatameter(parametersString, spliterator);
				} else {
					break;
				}
			}
		}
		return parameters;
	}

	private static String nextPatameter(String parametersString, String spliterator) {
		if (!parametersString.contains(spliterator)) {
			return "";
		}
		return parametersString.substring(parametersString.indexOf(spliterator) + spliterator.length());
	}

	private static String findKey(String parametersString) {
		return parametersString.substring(0, parametersString.indexOf(EQUALITY_SIGN));
	}

	private static String findValue(String parametersString, String spliterator) {
		if (parametersString.indexOf(EQUALITY_SIGN) + EQUALITY_SIGN.length() == parametersString.length()) {
			return "";
		}

		int spliteratorIndex = parametersString.indexOf(spliterator);
		int endIndex = spliteratorIndex < 0 ? parametersString.length() : spliteratorIndex;
		return parametersString.substring(parametersString.indexOf(EQUALITY_SIGN) + 1, endIndex);
	}
}
