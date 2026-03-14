package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SlotIcon {
    @Nullable ItemStack icon(SlotContext context);
}
