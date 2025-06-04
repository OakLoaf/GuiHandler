package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;

@FunctionalInterface
public interface IconProvider {
    @Nullable ItemStack icon(Gui gui, Slot slot);
}
