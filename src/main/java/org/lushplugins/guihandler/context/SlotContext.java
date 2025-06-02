package org.lushplugins.guihandler.context;

import org.lushplugins.guihandler.gui.Gui;

public class SlotContext {
    private final Gui gui;
    private final int slot;

    public SlotContext(Gui gui, int slot) {
        this.gui = gui;
        this.slot = slot;
    }

    public Gui gui() {
        return this.gui;
    }

    public int slot() {
        return this.slot;
    }
}
