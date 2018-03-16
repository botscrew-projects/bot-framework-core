package com.botscrew.botframework.container;

import com.botscrew.botframework.domain.ArgumentKit;
import com.botscrew.botframework.domain.DefaultArgumentKit;
import com.botscrew.botframework.domain.SimpleArgumentWrapper;
import com.botscrew.botframework.exception.ProcessorInnerException;
import com.botscrew.botframework.model.*;
import com.botscrew.botframework.util.ParametersUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
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

    public void process(ChatUser user, String intent, ArgumentKit originalKit) {
        IntentMethodKey key = new IntentMethodKey(user.getState(), intent);
        Optional<IntentInstanceMethod> instanceMethod = intentMethodGroup.find(key);

        if (!instanceMethod.isPresent()) {
            throw new IllegalArgumentException("No eligible method found for state: " + user.getState() + " and intent: " + intent);
        }

        originalKit.put(ArgumentType.USER, new SimpleArgumentWrapper(user));
        Map<String, String> parameters = ParametersUtils.getParameters(user.getState(), spliterator);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            originalKit.put(entry.getKey(), new SimpleArgumentWrapper(entry.getValue()));
        }

        tryInvokeMethod(instanceMethod.get(), originalKit);
    }

    private void tryInvokeMethod(AbstractMethod instanceMethod, ArgumentKit kit) {
        try {
            Object instance = instanceMethod.getInstance();
            Object[] args = instanceMethod.composeArgs(kit);
            instanceMethod.getMethod().invoke(instance, args);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ProcessorInnerException("Cannot process instance method", e);
        }
    }

}
