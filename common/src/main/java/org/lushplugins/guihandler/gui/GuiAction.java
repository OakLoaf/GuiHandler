package org.lushplugins.guihandler.gui;

public enum GuiAction {
    OPEN,
    REFRESH,
    POST_REFRESH,
    CLOSE;

    @FunctionalInterface
    public interface Callable {
        void call(GuiContext context);
    }
}
