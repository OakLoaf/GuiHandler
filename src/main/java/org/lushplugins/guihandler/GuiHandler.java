package org.lushplugins.guihandler;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.guihandler.slot.*;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.listener.InventoryListener;

import java.util.*;

public final class GuiHandler {
    private final JavaPlugin plugin;
    private final List<Listener> listeners = new ArrayList<>();
    private final Map<UUID, Gui> openGuis = new HashMap<>();
    private final Map<String, ButtonProvider> buttonTypes;
    private final Map<Character, SlotProvider> defaultProviders;

    private GuiHandler(JavaPlugin plugin, Map<String, ButtonProvider> buttonTypes, Map<Character, SlotProvider> defaultProviders) {
        this.plugin = plugin;
        this.buttonTypes = buttonTypes;
        this.defaultProviders = defaultProviders;

        registerListener(new InventoryListener(this), plugin);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Gui.Builder guiBuilder(InventoryType inventoryType) {
        return Gui.builder(this, inventoryType);
    }

    public Gui.Builder guiBuilder(int size) {
        return Gui.builder(this, size);
    }

    public Gui getOpenGui(UUID uuid) {
        return this.openGuis.get(uuid);
    }

    public void removeOpenGui(UUID uuid) {
        this.openGuis.remove(uuid);
    }

    public ButtonProvider getButtonProvider(String id) {
        return this.buttonTypes.get(id);
    }

    public SlotProvider getDefaultProvider(char label) {
        return this.defaultProviders.get(label);
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
        private final Map<Character, SlotProvider> defaultLabels = new HashMap<>();

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

        public Builder registerLabelProvider(char label, SlotProvider provider) {
            this.defaultLabels.put(label, provider);
            return this;
        }

        public GuiHandler build() {
            return new GuiHandler(this.plugin, this.buttons, this.defaultLabels);
        }
    }
}
