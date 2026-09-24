package org.lushplugins.guihandler.config.gui;

import org.bukkit.inventory.ItemStack;
import org.lushplugins.guihandler.config.GuiConfig;
import org.lushplugins.guihandler.config.slot.SlotConfig;
import org.lushplugins.guihandler.gui.PagedGui;
import org.lushplugins.guihandler.slot.SlotContext;

import java.util.Map;

public interface ConfiguredPagedGui<T> extends PagedGui<T> {

    GuiConfig getGuiConfig();

    private ItemStack getPageIcon(SlotContext context, boolean active, char label) {
        Map<Character, SlotConfig> slots = getGuiConfig().slots();
        SlotConfig slot = slots.get(label);
        if (slot == null) {
            return null;
        }

        if (!active) {
            char fallback = slot.fallback();
            if (slots.containsKey(fallback)) {
                slot = slots.get(fallback);
            }
        }

        return slot != null ? slot.icon().icon(context) : null;
    }

    @Override
    default ItemStack getNextPageIcon(SlotContext context, boolean active) {
        return getPageIcon(context, active, '>');
    }

    @Override
    default ItemStack getPreviousPageIcon(SlotContext context, boolean active) {
        return getPageIcon(context, active, '<');
    }
}
