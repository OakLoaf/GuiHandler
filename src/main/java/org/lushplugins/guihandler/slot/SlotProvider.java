package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface SlotProvider extends IconProvider, ButtonProvider {

    static SlotProvider create(@NotNull IconProvider iconProvider, @NotNull ButtonProvider buttonProvider) {
        return new SlotProvider() {

            @Override
            public ItemStack icon() {
                return iconProvider.icon();
            }

            @Override
            public Button button() {
                return buttonProvider.button();
            }
        };
    }

    static SlotProvider create(IconProvider iconProvider) {
        return create(iconProvider, () -> null);
    }

    static SlotProvider create(ItemStack item) {
        return create(() -> item);
    }

    static SlotProvider create(ButtonProvider buttonProvider) {
        return create(() -> null, buttonProvider);
    }

    static SlotProvider create(Button button) {
        return create(() -> button);
    }
}
