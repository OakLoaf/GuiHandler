package org.lushplugins.guihandler.slot;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.lushplugins.guihandler.button.ButtonProvider;

public class Slot {
    private final int slot;
    private final char label;
    private boolean locked;
    private ButtonProvider buttonProvider;

    public Slot(int slot, char label) {
        this.slot = slot;
        this.label = label;
    }

    public int slot() {
        return this.slot;
    }

    public char label() {
        return this.label;
    }

    public boolean locked() {
        return this.locked;
    }

    public void locked(boolean lock) {
        this.locked = lock;
    }

    public ButtonProvider buttonProvider() {
        return this.buttonProvider;
    }

    public void click(InventoryClickEvent event) {
        this.buttonProvider.button().click(event);
    }

    public void buttonProvider(ButtonProvider buttonProvider) {
        this.buttonProvider = buttonProvider;
    }
}
