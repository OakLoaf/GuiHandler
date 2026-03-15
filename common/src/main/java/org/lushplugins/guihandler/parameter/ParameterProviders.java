package org.lushplugins.guihandler.parameter;

import org.bukkit.inventory.Inventory;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.annotation.SlotAt;
import org.lushplugins.guihandler.annotation.LabelledSlots;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiActor;
import org.lushplugins.guihandler.slot.SlotContext;

import java.util.Map;

public class ParameterProviders {
    public static final Map<Class<?>, ParameterProvider<?>> DEFAULT_PROVIDERS = Map.ofEntries(
        ParameterProvider.Factory.forType(SlotContext.class, (type, context) -> context),
        ParameterProvider.Factory.forType(GuiHandler.class, (type, context) -> context.gui().instance()),
        ParameterProvider.Factory.forType(Gui.class, (type, context) -> context.gui()),
        ParameterProvider.Factory.forType(org.lushplugins.guihandler.slot.Slot.class, (type, context) -> context.slot()),
        ParameterProvider.Factory.forType(GuiActor.class, (type, context) -> context.gui().actor()),
        ParameterProvider.Factory.forType(Inventory.class, (type, context) -> context.gui().inventory())
    );

    public static final ParameterProvider<?> PROVIDED_TYPE = (type, context) -> context.gui().provided(type);


    public static ParameterProvider<?> createSlotProvider(SlotAt annotation) {
        int rawSlot = annotation.value();
        return (type, context) -> context.gui().slot(rawSlot);
    }

    public static ParameterProvider<?> createSlotsProvider(LabelledSlots annotation) {
        char[] labels = annotation.value();

        if (labels.length == 1 && labels[0] == LabelledSlots.DEFAULT_LABEL) {
            return (type, context) -> context.gui().slots();
        } else {
            return (type, context) -> context.gui().slots(slot -> {
                for (char label : labels) {
                    if (label == slot.label()) {
                        return true;
                    }
                }

                return false;
            });
        }
    }
}
