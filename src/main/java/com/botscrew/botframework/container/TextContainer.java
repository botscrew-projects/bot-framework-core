package com.botscrew.botframework.container;

import com.botscrew.botframework.annotation.Text;
import com.botscrew.botframework.exception.DuplicatedActionException;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.exception.WrongMethodSignatureException;
import com.botscrew.botframework.model.ArgumentType;
import com.botscrew.botframework.model.ChatUser;
import com.botscrew.botframework.model.InstanceMethod;
import com.botscrew.botframework.util.ParametersUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TextContainer extends AbstractContainer {

    private final Map<String, InstanceMethod> textActionsMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(TextContainer.class);

    public TextContainer() {
        super();
        textActionsMap = new ConcurrentHashMap<>();
    }

    public TextContainer(String spliterator) {
        super(spliterator);
        textActionsMap = new ConcurrentHashMap<>();
    }

    @Override
    public void register(Object object) {
        Stream.of(object.getClass().getMethods()).filter(m -> m.isAnnotationPresent(Text.class)).forEach(m -> {
            List<ArgumentType> arguments = getArgumentTypes(m);
            InstanceMethod instanceMethod = new InstanceMethod(object, m, arguments, Arrays.asList(m.getParameters()));
			Text text = m.getAnnotation(Text.class);

            if (text.states().length == 0) {
                addAction(ALL_STATES, instanceMethod);
            } else {
                for (String state : text.states()) {
                    addAction(state, instanceMethod);
                }
            }

            LOGGER.debug("Added method " + m.getName() + " with parameters: \n" + arguments.toString());
        });
    }

    public void processText(String text, ChatUser user) {
        InstanceMethod instanceMethod = findInstanceMethod(user);
		Object[] arguments = getArguments(instanceMethod.getArgumentTypes(), instanceMethod.getParameters(), text, user);
		try {
			instanceMethod.getMethod().invoke(instanceMethod.getInstance(), arguments);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Error while text processing", e);
			throw new ProcessorInnerException(e);
		}
	}

    private InstanceMethod findInstanceMethod(ChatUser user) {
        String stateValue = ParametersUtils.getValueWithoutParams(user.getState(), spliterator);
        InstanceMethod instanceMethod = textActionsMap.get(stateValue);

        if (instanceMethod == null) {
            instanceMethod = textActionsMap.get(ALL_STATES);
            if (instanceMethod == null) {
                throw new IllegalArgumentException(
                        "No method with annotation @Text which meets given parameters,  state:" + user.getState());
            }
        }

        return instanceMethod;
    }

    private void addAction(String state, InstanceMethod instanceMethod) {
        if (textActionsMap.containsKey(state)) {
            throw new DuplicatedActionException("Duplication of text processing action: state = " + state);
        }
        textActionsMap.put(state, instanceMethod);
    }

    @Override
    protected ArgumentType getArgumentType(Parameter parameter) {
        if (parameter.isAnnotationPresent(Text.class)) return ArgumentType.TEXT;

        if (ChatUser.class.isAssignableFrom(parameter.getType())) return ArgumentType.USER;

        if (Map.class.isAssignableFrom(parameter.getType())) return ArgumentType.STATE_PARAMETERS;

        Optional<ArgumentType> argumentTypeOpt = getBaseTypeArgumentByClass(parameter.getType());

        return argumentTypeOpt.orElseThrow(() -> new WrongMethodSignatureException(
                "Methods can only contain next parameters: \n" +
                        "ChatUser implementation, Map, String, Long, Integer, Short, Byte, Double, Float"));
    }

    private Object[] getArguments(List<ArgumentType> types, List<Parameter> parameters, String text, ChatUser user) {
        final Object[] result = new Object[types.size()];
        Map<String, String> stateParameters = ParametersUtils.getParameters(user.getState(), spliterator);

        IntStream.range(0, types.size()).forEach(index -> {

            switch (types.get(index)) {
                case USER:
                    result[index] = convertUser(user, parameters.get(index));
                    break;
                case TEXT:
                    result[index] = text;
                    break;
                case STATE_PARAMETERS:
                    result[index] = stateParameters;
                    break;
                case PARAM_STRING:
					String name = getParamName(parameters.get(index));
					result[index] = stateParameters.get(name);
                    break;
				case PARAM_BYTE:
					name = getParamName(parameters.get(index));
					result[index] = Byte.valueOf(stateParameters.get(name));
					break;
				case PARAM_SHORT:
                    name = getParamName(parameters.get(index));
					result[index] = Short.valueOf(stateParameters.get(name));
					break;
				case PARAM_INTEGER:
                    name = getParamName(parameters.get(index));
					result[index] = Integer.valueOf(stateParameters.get(name));
					break;
				case PARAM_LONG:
                    name = getParamName(parameters.get(index));
					result[index] = Long.valueOf(stateParameters.get(name));
					break;
				case PARAM_FLOAT:
                    name = getParamName(parameters.get(index));
					result[index] = Float.valueOf(stateParameters.get(name));
					break;
				case PARAM_DOUBLE:
                    name = getParamName(parameters.get(index));
					result[index] = Double.valueOf(stateParameters.get(name));
					break;
                default:
                    result[index] = null;
            }

        });
        return result;
    }
}
