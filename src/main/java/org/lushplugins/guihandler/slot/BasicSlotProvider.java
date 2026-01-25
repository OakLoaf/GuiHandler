package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ClassCanBeRecord")
public class BasicSlotProvider implements SlotProvider {
    private final IconProvider iconProvider;
    private final Button button;

    public BasicSlotProvider(IconProvider iconProvider, Button button) {
        this.iconProvider = iconProvider;
        this.button = button;
    }

    public IconProvider iconProvider() {
        return this.iconProvider;
    }

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return this.iconProvider.icon(context);
    }

    public Button button() {
        return this.button;
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
