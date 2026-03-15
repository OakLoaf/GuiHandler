package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ClassCanBeRecord")
public class BasicSlotProvider implements SlotProvider {
    private final SlotIcon icon;
    private final SlotAction action;

    public BasicSlotProvider(@Nullable SlotIcon icon, @Nullable SlotAction action) {
        this.icon = icon;
        this.action = action;
    }

    @Override
    public boolean hasIcon() {
        return icon != null;
    }

    public SlotIcon icon() {
        return this.icon;
    }

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return this.icon != null ? this.icon.icon(context) : null;
    }

    @Override
    public boolean hasAction() {
        return action != null;
    }

    public SlotAction action() {
        return this.action;
    }

    @Override
    public void click(SlotContext context, InventoryClickEvent event) {
        if (this.action != null) {
            action.click(context, event);
        }
    }

    @Override
    public SlotProvider with(@Nullable SlotIcon icon) {
        return new BasicSlotProvider(icon, this.action);
    }

    @Override
    public SlotProvider with(@Nullable SlotAction action) {
        return new BasicSlotProvider(this.icon, action);
    }
}
