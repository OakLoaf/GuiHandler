package org.lushplugins.guihandler.listener;

import org.bukkit.event.Listener;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    private final GuiHandler guiHandler;

    public InventoryListener(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Gui gui = this.guiHandler.getOpenGui(player.getUniqueId());
        if (gui == null) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !player.getOpenInventory().getTopInventory().equals(gui.getInventory())) {
            return;
        }

        gui.onClick(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Gui gui = this.guiHandler.getOpenGui(player.getUniqueId());
        if (gui == null || !event.getInventory().equals(gui.getInventory())) {
            return;
        }

        gui.onDrag(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Gui gui = this.guiHandler.getOpenGui(player.getUniqueId());
        if (gui == null || !event.getInventory().equals(gui.getInventory())) {
            return;
        }

        this.guiHandler.removeOpenGui(player.getUniqueId());
        gui.onClose(event);
    }
}
