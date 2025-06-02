package org.lushplugins.guihandler;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.guihandler.button.Button;
import org.lushplugins.guihandler.button.ButtonProvider;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.gui.GuiProvider;
import org.lushplugins.guihandler.listener.InventoryListener;

import java.util.*;

public final class GuiHandler {
    private final JavaPlugin plugin;
    private final List<Listener> listeners = new ArrayList<>();
    private final Map<UUID, Gui> openGuis = new HashMap<>();
    private final Map<String, GuiProvider> guis = new HashMap<>();
    private final Map<String, ButtonProvider> buttons;

    private GuiHandler(JavaPlugin plugin, Map<String, ButtonProvider> buttons) {
        this.plugin = plugin;
        this.buttons = buttons;

        registerListener(new InventoryListener(this), plugin);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Gui getOpenGui(UUID uuid) {
        return this.openGuis.get(uuid);
    }

    public void removeOpenGui(UUID uuid) {
        this.openGuis.remove(uuid);
    }

    public GuiProvider getGuiProvider(String id) {
        return this.guis.get(id);
    }

    public void registerGui(String id, GuiProvider guiProvider) {
        this.guis.put(id, guiProvider);
    }

    public ButtonProvider getButtonProvider(String id) {
        return this.buttons.get(id);
    }

    private void registerListener(Listener listener, JavaPlugin plugin) {
        this.listeners.add(listener);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void unregisterListeners() {
        this.listeners.forEach(HandlerList::unregisterAll);
        this.listeners.clear();
    }

    public static Builder builder(JavaPlugin plugin) {
        return new Builder(plugin);
    }

    public static class Builder {
        private final JavaPlugin plugin;
        private final Map<String, ButtonProvider> buttons = new HashMap<>();

        private Builder(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public Builder registerButtonType(String id, ButtonProvider provider) {
            this.buttons.put(id, provider);
            return this;
        }

        public Builder registerButtonType(String id, Button button) {
            return registerButtonType(id, () -> button);
        }

        public GuiHandler build() {
            return new GuiHandler(this.plugin, this.buttons);
        }
    }
}
