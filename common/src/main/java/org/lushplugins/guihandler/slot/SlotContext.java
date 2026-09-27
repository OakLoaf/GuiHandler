package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;

public record SlotContext(Gui gui, Slot slot, @Nullable InventoryClickEvent clickEvent) {

    public SlotContext(Gui gui, Slot slot) {
        this(gui, slot, null);
    }
}
