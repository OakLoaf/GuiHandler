package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;

public class Slot {
    private final int rawSlot;
    private char label;
    private Integer labelIndex;
    private boolean locked = true;
    private @Nullable SlotIcon icon;
    private @Nullable SlotAction action;

    public Slot(int rawSlot, char label, @Nullable Integer labelIndex) {
        this.rawSlot = rawSlot;
        this.label = label;
        this.labelIndex = labelIndex;
    }

    public Slot(int rawSlot, char label) {
        this(rawSlot, label, null);
    }

    public int rawSlot() {
        return this.rawSlot;
    }

    public char label() {
        return this.label;
    }

    public void label(char label) {
        this.label = label;
    }

    public @Nullable Integer labelIndex() {
        return this.labelIndex;
    }

    public void labelIndex(@Nullable Integer labelIndex) {
        this.labelIndex = labelIndex;
    }

    public boolean locked() {
        return this.locked;
    }

    public void lock(boolean lock) {
        this.locked = lock;
    }

    public void icon(SlotIcon icon) {
        this.icon = icon;
    }

    public void icon(ItemStack icon) {
        this.icon((context) -> icon);
    }

    public @Nullable ItemStack icon(Gui gui) {
        return this.icon != null ? this.icon.icon(new SlotContext(gui, this)) : null;
    }

    public void action(@Nullable SlotAction action) {
        this.action = action;
    }

    public void click(Gui gui, InventoryClickEvent event) {
        if (this.action != null) {
            this.action.click(new SlotContext(gui, this), event);
        }
    }

    public void slotProvider(SlotProvider slotProvider) {
        this.icon(slotProvider);
        this.action(slotProvider);
    }
}
