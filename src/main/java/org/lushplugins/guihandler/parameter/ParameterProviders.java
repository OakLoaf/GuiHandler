package org.lushplugins.guihandler.parameter;

import org.bukkit.inventory.Inventory;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.annotation.Slot;
import org.lushplugins.guihandler.annotation.Slots;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiActor;

import java.util.Arrays;
import java.util.Map;

public class ParameterProviders {
    public static final Map<Class<?>, ParameterProvider<?>> DEFAULT_PROVIDERS = Map.ofEntries(
        ParameterProvider.Factory.forType(GuiHandler.class, (type, context) -> context.gui().instance()),
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
        char[] labels = annotation.value();

        if (labels.length == 1 && labels[0] == Slots.DEFAULT_LABEL) {
            return (type, context) -> context.gui().slots();
        } else {
            return (type, context) -> Arrays.stream(context.gui().slots())
                .filter(slot -> {
                    for (char label : labels) {
                        if (label == slot.label()) {
                            return true;
                        }
                    }

                    return false;
                })
                .toList();
        }
    }
}
