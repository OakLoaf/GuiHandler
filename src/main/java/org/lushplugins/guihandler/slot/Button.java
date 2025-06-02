package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiActor;

@FunctionalInterface
public interface Button {
    void click(GuiActor actor, Gui gui, Slot slot);

    default void click(InventoryClickEvent event) {}
}
