package org.lushplugins.guihandler.config.slot.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.slot.SlotAction;
import org.lushplugins.guihandler.slot.SlotContext;

public class PreviousPageSlotAction implements SlotAction {

    @Override
    public void click(SlotContext context, InventoryClickEvent event) {
        Gui gui = context.gui();
        if (gui.page() > 1) {
            gui.previousPage();
        }
    }
}
