package org.lushplugins.guihandler.gui.event;

import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.slot.Slot;

public class GuiSlotRefreshEvent extends GuiEvent {
    private final Slot slot;

    public GuiSlotRefreshEvent(Gui gui, Slot slot) {
        super(gui);
        this.slot = slot;
    }

    public Slot slot() {
        return slot;
    }
}
