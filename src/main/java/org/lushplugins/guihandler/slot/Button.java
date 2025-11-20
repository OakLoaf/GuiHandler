package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface Button {
    void click(InventoryClickEvent event, SlotContext context);

    Button EMPTY = (ignored1, ignored2) -> {};
}
