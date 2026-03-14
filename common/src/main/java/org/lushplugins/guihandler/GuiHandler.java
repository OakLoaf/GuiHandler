package org.lushplugins.guihandler;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.guihandler.annotation.AnnotationHandler;
import org.lushplugins.guihandler.slot.*;
import org.lushplugins.guihandler.gui.Gui;
import org.lushplugins.guihandler.listener.InventoryListener;

import java.util.*;

public final class GuiHandler {
    private final JavaPlugin plugin;
    private final Map<Character, SlotProvider> defaultLabelProviders;
    private final List<Listener> listeners = new ArrayList<>();
    private final Map<UUID, Gui> openGuis = new HashMap<>();

    private GuiHandler(JavaPlugin plugin, Map<Character, SlotProvider> defaultProviders) {
        this.plugin = plugin;
        this.defaultLabelProviders = defaultProviders;

        registerListener(new InventoryListener(this), plugin);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Gui.Builder prepare(Object instance) {
        Class<?> instanceClass = instance instanceof Class ? (Class<?>) instance : instance.getClass();
        return AnnotationHandler.register(this, instanceClass, instance);
    }

    public Gui.Builder guiBuilder() {
        return Gui.builder(this);
    }

    public Gui.Builder guiBuilder(InventoryType inventoryType) {
        return Gui.builder(this)
            .inventoryType(inventoryType);
    }

    public Gui getOpenGui(UUID uuid) {
        return this.openGuis.get(uuid);
    }

    public void setOpenGui(UUID uuid, Gui gui) {
        this.openGuis.put(uuid, gui);
    }

    public void removeOpenGui(UUID uuid) {
        this.openGuis.remove(uuid);
    }

    public SlotProvider getDefaultProvider(char label) {
        return this.defaultLabelProviders.get(label);
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
        private final Map<Character, SlotProvider> defaultLabelProviders = new HashMap<>();

        private Builder(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public Builder registerLabelProvider(char label, SlotProvider provider) {
            this.defaultLabelProviders.put(label, provider);
            return this;
        }

        public GuiHandler build() {
            return new GuiHandler(this.plugin, this.defaultLabelProviders);
        }
    }
}
