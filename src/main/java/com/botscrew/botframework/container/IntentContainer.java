package com.botscrew.botframework.container;

import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.model.*;
import com.botscrew.botframework.util.ParametersUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IntentContainer extends AbstractContainer {
    private final IntentMethodGroup intentMethodGroup;

    @Override
    public void register(Object object) {
        // TODO: 11.03.18 remove
    }

    @Override
    protected ArgumentType getArgumentType(Parameter parameter) {
        // TODO: 11.03.18 remove
        return null;
    }

    public IntentContainer(IntentMethodGroup intentMethodGroup) {
        this.intentMethodGroup = intentMethodGroup;
    }

    public void process(ChatUser user, String intent, Map<String, Object> params) {
        IntentMethodKey key = new IntentMethodKey(user.getState(), intent);
        Optional<IntentInstanceMethod> instanceMethod = intentMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method found for state: " + user.getState() + " and intent: " + intent);
        }

        tryInvokeMethod(instanceMethod.get(), user, params);
    }

    private void tryInvokeMethod(AbstractMethod instanceMethod, ChatUser user, Map<String, Object> params) {
        try {
            instanceMethod.getMethod().invoke(instanceMethod.getInstance(), getParams(instanceMethod, user, params));
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProcessorInnerException("Cannot process instance method", e);
        }
    }

    private Object[] getParams(AbstractMethod instanceMethod, ChatUser user, Map<String, Object> params) {
        List<Argument> arguments = instanceMethod.getArguments();
        Object[] result = new Object[arguments.size()];

        Map<String, String> stateParameters = ParametersUtils.getParameters(user.getState(), spliterator);

        for (int i = 0; i < arguments.size(); i++) {
            Argument argument = arguments.get(i);

            switch (argument.getType()) {
                case USER:
                    result[i] = convertUser(user, argument.getParameter());
                    break;
                case NLP_PARAMS:
                    result[i] = params;
                    break;
                case STATE_PARAMETERS:
                    result[i] = stateParameters;
                    break;
                case PARAM_STRING:
                    result[i] = stateParameters.get(argument.getName());
                    break;
                case PARAM_INTEGER:
                    result[i] = Integer.valueOf(stateParameters.get(argument.getName()));
                    break;
                case PARAM_LONG:
                    result[i] = Long.valueOf(stateParameters.get(argument.getName()));
                    break;
                case PARAM_BYTE:
                    result[i] = Byte.valueOf(stateParameters.get(argument.getName()));
                    break;
                case PARAM_SHORT:
                    result[i] = Short.valueOf(stateParameters.get(argument.getName()));
                    break;
                case PARAM_FLOAT:
                    result[i] = Float.valueOf(stateParameters.get(argument.getName()));
                    break;
                case PARAM_DOUBLE:
                    result[i] = Double.valueOf(stateParameters.get(argument.getName()));
                    break;
                default:
                    result[i] = null;
            }
        }
        return result;
    }

}
