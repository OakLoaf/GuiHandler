package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IconProvider {
    @Nullable ItemStack icon(SlotContext context);

    IconProvider EMPTY = (context) -> null;
}
