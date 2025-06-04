package org.lushplugins.guihandler.slot;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ButtonProvider {
    @Nullable Button button();
}
