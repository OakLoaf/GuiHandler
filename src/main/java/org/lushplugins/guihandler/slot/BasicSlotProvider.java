package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record BasicSlotProvider(IconProvider iconProvider, Button button) implements SlotProvider {

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return this.iconProvider.icon(context);
    }

    @Override
    public void click(InventoryClickEvent event, SlotContext context) {
        this.button.click(event, context);
    }

    @Override
    public SlotProvider with(IconProvider iconProvider) {
        return new BasicSlotProvider(iconProvider, this.button);
    }

    @Override
    public SlotProvider with(Button button) {
        return new BasicSlotProvider(this.iconProvider, button);
    }
}
