package org.lushplugins.guihandler.slot;

import org.lushplugins.guihandler.gui.Gui;

import java.util.List;

@FunctionalInterface
public interface LabelledSlotProvider {
    void apply(Gui gui, char label, List<Slot> slots);
}
