package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface SlotAction {
    void click(SlotContext context, InventoryClickEvent event);
}
