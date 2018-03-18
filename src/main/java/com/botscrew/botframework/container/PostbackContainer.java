package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.SimpleArgumentKit;
import com.botscrew.botframework.domain.SimpleArgumentWrapper;
import com.botscrew.botframework.domain.method.HandlingMethod;
import com.botscrew.botframework.domain.method.group.PostbackHandlingMethodGroup;
import com.botscrew.botframework.domain.method.key.BiMethodKey;
import com.botscrew.botframework.domain.param.SimpleStringParametersDetector;
import com.botscrew.botframework.domain.param.StringParametersDetector;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;

import java.util.Map;
import java.util.Optional;

public class PostbackContainer extends AbstractContainer {
	private final PostbackHandlingMethodGroup postbackHandlingMethodGroup;
	private final StringParametersDetector stringParametersDetector;

	public PostbackContainer(PostbackHandlingMethodGroup postbackHandlingMethodGroup) {
		this.postbackHandlingMethodGroup = postbackHandlingMethodGroup;
		this.stringParametersDetector = new SimpleStringParametersDetector();
	}

	public PostbackContainer(PostbackHandlingMethodGroup postbackHandlingMethodGroup, StringParametersDetector stringParametersDetector) {
		this.postbackHandlingMethodGroup = postbackHandlingMethodGroup;
		this.stringParametersDetector = stringParametersDetector;
	}

	public void process(ChatUser user, String postback, ArgumentKit originalKit) {
		if (originalKit == null) originalKit = new SimpleArgumentKit();

		String stateWithoutParams = stringParametersDetector.getValueWithoutParams(user.getState());
		String postbackWithoutParams = stringParametersDetector.getValueWithoutParams(postback);
		BiMethodKey key = new BiMethodKey(stateWithoutParams, postbackWithoutParams);
		Optional<HandlingMethod> instanceMethod = postbackHandlingMethodGroup.find(key);

		if (!instanceMethod.isPresent()) {
			throw new IllegalArgumentException("No eligible method with @Postback annotation found for state: " +
					stateWithoutParams + " and postback: " + postbackWithoutParams);
		}
		Map<String, String> stateParameters = stringParametersDetector.getParameters(user.getState());
		Map<String, String> postbackParameters = stringParametersDetector.getParameters(postback);

		originalKit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
		originalKit.put(ArgumentType.POSTBACK, new SimpleArgumentWrapper(postback));
		originalKit.put(ArgumentType.STATE_PARAMETERS, new SimpleArgumentWrapper(stateParameters));
		originalKit.put(ArgumentType.POSTBACK_PARAMETERS, new SimpleArgumentWrapper(postbackParameters));
		for (Map.Entry<String, String> entry : stateParameters.entrySet()) {
			originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
		}
		for (Map.Entry<String, String> entry : postbackParameters.entrySet()) {
			originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
		}

		tryInvokeMethod(instanceMethod.get(), originalKit);
	}
}
