package org.lushplugins.guihandler;

import org.bukkit.plugin.java.JavaPlugin;

public final class GuiHandler extends JavaPlugin {
    private static GuiHandler plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static GuiHandler getInstance() {
        return plugin;
    }
}
