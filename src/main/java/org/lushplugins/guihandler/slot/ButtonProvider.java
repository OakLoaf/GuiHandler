package org.lushplugins.guihandler.slot;

import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.gui.Gui;

// TODO: Consider merging with Button
@FunctionalInterface
public interface ButtonProvider {
    @Nullable Button button(Gui gui, Slot slot);
}
