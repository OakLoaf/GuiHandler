package org.lushplugins.guihandler.slot;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;

@FunctionalInterface
public interface ButtonProvider {
    @Nullable Button button(Gui gui, Slot slot);
}
