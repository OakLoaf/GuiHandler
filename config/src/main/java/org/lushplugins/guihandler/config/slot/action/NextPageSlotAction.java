package org.lushplugins.guihandler.config.slot.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.slot.SlotAction;
import org.lushplugins.guihandler.slot.SlotContext;

public class NextPageSlotAction implements SlotAction {

    @Override
    public void click(SlotContext context, InventoryClickEvent event) {
        context.gui().nextPage();
    }
}
