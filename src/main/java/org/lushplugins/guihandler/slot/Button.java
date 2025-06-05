package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface Button {
    void click(SlotContext context);

    default void click(InventoryClickEvent event) {}
}
