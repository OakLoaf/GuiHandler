package org.lushplugins.guihandler.parameter;

import org.lushplugins.guihandler.slot.SlotContext;

public record GuiParameter<T>(String name, Class<?> type, ParameterProvider<T> provider) {

    public Object asObject(SlotContext context) {
        return this.provider.collect((Class<T>) this.type, context);
    }
}
