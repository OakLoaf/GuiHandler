package org.lushplugins.guihandler.parameter;

import org.bukkit.inventory.Inventory;
import org.lushplugins.guihandler.annotation.Slot;
import org.lushplugins.guihandler.annotation.Slots;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiActor;

import java.util.Arrays;
import java.util.Map;

public class ParameterProviders {
    public static final Map<Class<?>, ParameterProvider<?>> DEFAULT_PROVIDERS = Map.ofEntries(
        ParameterProvider.Factory.forType(Gui.class, (type, context) -> context.gui()),
        ParameterProvider.Factory.forType(org.lushplugins.guihandler.slot.Slot.class, (type, context) -> context.slot()),
        ParameterProvider.Factory.forType(GuiActor.class, (type, context) -> context.gui().actor()),
        ParameterProvider.Factory.forType(Inventory.class, (type, context) -> context.gui().inventory())
    );

    public static final ParameterProvider<?> PROVIDED_TYPE = (type, context) -> context.gui().provided(type);


    public static ParameterProvider<?> createSlotProvider(Slot annotation) {
        int rawSlot = annotation.value();
        return (type, context) -> context.gui().slot(rawSlot);
    }

    public static ParameterProvider<?> createSlotsProvider(Slots annotation) {
        char label = annotation.value();

        if (label == Slots.DEFAULT_LABEL) {
            return (type, context) -> context.gui().slots();
        } else {
            return (type, context) -> Arrays.stream(context.gui().slots())
                .filter(slot -> slot.label() == label)
                .toList();
        }
    }
}
