package org.lushplugins.guihandler.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.GuiHandler;

public interface Button {

    void click(InventoryClickEvent event);

    default @Nullable ItemStack icon() {
        return null;
    }

    interface Factory {

        @Nullable
        Button create(@NotNull GuiHandler guiHandler);
    }
}
