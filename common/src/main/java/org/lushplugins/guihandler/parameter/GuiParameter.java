package org.lushplugins.guihandler.parameter;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.event.GuiEvent;
import org.lushplugins.guihandler.slot.SlotContext;

public record GuiParameter<T>(String name, Class<?> type, ParameterProvider<T> provider) {

    public Object asObject(@Nullable GuiEvent event, SlotContext context) {
        return this.provider.collect((Class<T>) this.type, event, context);
    }
}
