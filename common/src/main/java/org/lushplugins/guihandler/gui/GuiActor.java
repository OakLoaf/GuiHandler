package org.lushplugins.guihandler.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class GuiActor {
    private final Player player;

    public GuiActor(Player player) {
        this.player = player;
    }

    public @NotNull UUID uuid() {
        return player.getUniqueId();
    }

    public @NotNull String name() {
        return player.getName();
    }

    public @NotNull Player player() {
        return player;
    }
}
