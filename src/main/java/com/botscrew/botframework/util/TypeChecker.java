package com.botscrew.botframework.util;

import java.util.Arrays;

public class TypeChecker {

	private TypeChecker() {
	}

	public static boolean isInterfaceImplementing(Class<?> type, Class<?> interfaceType) {
		return type.equals(interfaceType) || Arrays.stream(type.getInterfaces())
				.anyMatch(clazz -> clazz.equals(interfaceType));
	}
}
