package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.gui.Gui;

@FunctionalInterface
public interface Button {
    void click(Gui gui, Slot slot);

    default void click(InventoryClickEvent event) {}
}
