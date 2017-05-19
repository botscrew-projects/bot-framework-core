package com.botscrew.framework.flow.util;

import java.util.Arrays;

public class TypeChecker {

	private TypeChecker() {
	}

	public static boolean isInterfaceImplementing(Class<?> type, Class<?> interfaceType) {
		return type.equals(interfaceType) || Arrays.stream(type.getInterfaces())
				.filter(clazz -> clazz.equals(interfaceType)).findAny().isPresent();
	}
}
