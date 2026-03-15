package org.lushplugins.guihandler.gui;

public record GuiContext(Gui gui) {

    public GuiActor actor() {
        return gui.actor();
    }
}
