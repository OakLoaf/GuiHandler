package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiActor;

public class Slot {
    private final int rawSlot;
    private final char label;
    private final Integer labelIndex;
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

    public @Nullable Integer labelIndex() {
        return this.labelIndex;
    }

    public boolean locked() {
        return this.locked;
    }

    public void lock(boolean lock) {
        this.locked = lock;
    }

    public ItemStack icon() {
        return this.iconProvider.icon();
    }

    public void iconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider;
    }

    public void icon(ItemStack icon) {
        this.iconProvider(() -> icon);
    }

    public Button button() {
        return this.buttonProvider.button();
    }

    public void click(GuiActor actor, Gui gui) {
        this.button().click(actor, gui, this);
    }

    public void click(InventoryClickEvent event) {
        this.button().click(event);
    }

    public void buttonProvider(ButtonProvider buttonProvider) {
        this.buttonProvider = buttonProvider;
    }
}
