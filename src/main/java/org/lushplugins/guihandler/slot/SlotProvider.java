package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.guihandler.gui.Gui;

public interface SlotProvider extends IconProvider, ButtonProvider {

    static SlotProvider create(@NotNull IconProvider iconProvider, @NotNull ButtonProvider buttonProvider) {
        return new SlotProvider() {

            @Override
            public ItemStack icon(Gui gui, Slot slot) {
                return iconProvider.icon(gui, slot);
            }

            @Override
            public Button button(Gui gui, Slot slot) {
                return buttonProvider.button(gui, slot);
            }
        };
    }

    static SlotProvider create(IconProvider iconProvider) {
        return create(iconProvider, (gui, slot) -> null);
    }

    static SlotProvider create(ItemStack item) {
        return create((IconProvider) (gui, slot) -> item);
    }

    static SlotProvider create(ButtonProvider buttonProvider) {
        return create((gui, slot) -> null, buttonProvider);
    }

    static SlotProvider create(Button button) {
        return create((ButtonProvider) (gui, slot) -> button);
    }
}
