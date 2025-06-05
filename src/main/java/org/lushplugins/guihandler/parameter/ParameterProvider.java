package org.lushplugins.guihandler.parameter;

import org.lushplugins.guihandler.slot.SlotContext;

import java.util.Map;

@FunctionalInterface
public interface ParameterProvider<T> {
    T collect(Class<T> typeClass, SlotContext context);

    class Factory {

        public static <T> Map.Entry<Class<T>, ParameterProvider<T>> forType(Class<T> typeClass, ParameterProvider<T> constructor) {
            return Map.entry(typeClass, constructor);
        }
    }
}
