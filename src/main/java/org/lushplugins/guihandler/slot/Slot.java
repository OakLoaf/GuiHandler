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
    private IconProvider iconProvider;
    private ButtonProvider buttonProvider;

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

    public @Nullable ItemStack icon(Gui gui) {
        return this.iconProvider.icon(gui);
    }

    public void iconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider;
    }

    public void icon(ItemStack icon) {
        this.iconProvider((gui) -> icon);
    }

    public @Nullable Button button(Gui gui) {
        return this.buttonProvider.button(gui);
    }

    public void click(InventoryClickEvent event, Gui gui) {
        Button button = this.button(gui);
        if (null != button) {
            button.click(gui, this);
            button.click(event);
        }
    }

    public void buttonProvider(ButtonProvider buttonProvider) {
        this.buttonProvider = buttonProvider;
    }

    public void slotProvider(SlotProvider slotProvider) {
        this.iconProvider(slotProvider);
        this.buttonProvider(slotProvider);
    }
}
