package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface IconProvider {
    ItemStack icon();
}
