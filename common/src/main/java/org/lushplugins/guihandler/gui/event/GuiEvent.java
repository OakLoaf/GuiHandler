package org.lushplugins.guihandler.gui.event;

import org.lushplugins.guihandler.gui.Gui;

public class GuiEvent {
    private final Gui gui;

    public GuiEvent(Gui gui) {
        this.gui = gui;
    }

    public Gui gui() {
        return gui;
    }
}
