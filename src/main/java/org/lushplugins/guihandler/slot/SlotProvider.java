package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.guihandler.gui.Gui;

public interface SlotProvider extends IconProvider, ButtonProvider {

    static SlotProvider create(@NotNull IconProvider iconProvider, @NotNull ButtonProvider buttonProvider) {
        return new SlotProvider() {

            @Override
            public ItemStack icon(Gui gui) {
                return iconProvider.icon(gui);
            }

            @Override
            public Button button(Gui gui) {
                return buttonProvider.button(gui);
            }
        };
    }

    static SlotProvider create(IconProvider iconProvider) {
        return create(iconProvider, (gui) -> null);
    }

    static SlotProvider create(ItemStack item) {
        return create((IconProvider) (gui) -> item);
    }

    static SlotProvider create(ButtonProvider buttonProvider) {
        return create((gui) -> null, buttonProvider);
    }

    static SlotProvider create(Button button) {
        return create((ButtonProvider) (gui) -> button);
    }
}
