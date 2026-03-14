package org.lushplugins.guihandler.config.slot.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.slot.SlotAction;
import org.lushplugins.guihandler.slot.SlotContext;

// TODO: Work out better implementation that supports checking if the page is full
public class NextPageSlotAction implements SlotAction {

    @Override
    public void click(SlotContext context, InventoryClickEvent event) {
        context.gui().nextPage();
    }
}
