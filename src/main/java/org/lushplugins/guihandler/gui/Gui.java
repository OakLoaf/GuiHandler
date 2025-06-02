package org.lushplugins.guihandler.gui;

import com.google.common.collect.TreeMultimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lushplugins.chatcolorhandler.ModernChatColorHandler;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.button.Button;
import org.lushplugins.guihandler.button.ButtonProvider;
import org.lushplugins.guihandler.slot.Slot;

import java.util.List;

public class Gui {
    private final Inventory inventory;
    private final Player player;
    private final Slot[] slots;

    public Gui(int size, Component component, Player player) {
        this.inventory = Bukkit.getServer().createInventory(null, size, component);
        this.player = player;
        this.slots = new Slot[size];
    }

    public Gui(InventoryType inventoryType, Component component, Player player) {
        this.inventory = Bukkit.getServer().createInventory(null, inventoryType, component);
        this.player = player;
        this.slots = new Slot[inventoryType.getDefaultSize()];
    }

    public Gui(int size, String title, Player player) {
        this(size, ModernChatColorHandler.translate(title), player);
    }

    public Gui(InventoryType inventoryType, String title, Player player) {
        this(inventoryType, ModernChatColorHandler.translate(title), player);
    }

    public Gui(GuiLayer layer, String title, Player player) {
        this(layer.getSize(), title, player);
        applyLayer(layer);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public Slot getSlot(int slot) {
        return this.slots[slot] != null ? this.slots[slot] : new Slot(slot, ' ');
    }

    public Slot[] getSlots() {
        return slots;
    }

    public void applyLayer(GuiLayer layer) {
        TreeMultimap<Character, Integer> slotMap = layer.getSlotMap();
        for (char character : slotMap.keySet()) {
            ButtonProvider provider = layer.getButtonProvider(character);
            if (provider == null) {
                continue;
            }

            slotMap.get(character).forEach(slot -> this.getSlot(slot).buttonProvider(provider));
        }
    }

    public void refresh(int slot) {
        ItemStack item = this.getSlot(slot).buttonProvider().button().icon();
        this.inventory.setItem(slot, item);
    }

    public void refresh() {
        for (int slot = 0; slot < this.slots.length; slot++) {
            refresh(slot);
        }
    }

    public void open() {
        refresh();

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(LushLib.getInstance().getPlugin(), () -> this.player.openInventory(this.inventory));
        } else {
            player.openInventory(inventory);
        }

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> guiManager.addGui(player.getUniqueId(), this));
    }

    public void onClick(InventoryClickEvent event) {
        this.onClick(event, false);
    }

    public void onClick(InventoryClickEvent event, boolean cancelAll) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        int slot = event.getRawSlot();

        Button button = buttons.get(slot);
        if (button != null) {
            try {
                button.onClick(event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (cancelAll) {
            event.setCancelled(true);
            return;
        }

        switch (event.getAction()) {
            case COLLECT_TO_CURSOR -> event.setCancelled(true);
            case DROP_ALL_SLOT, DROP_ONE_SLOT, PLACE_ALL, PLACE_SOME, PLACE_ONE, PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE, SWAP_WITH_CURSOR, CLONE_STACK, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD  -> {
                if (clickedInventory.equals(inventory) && this.slots[slot].locked()) {
                    event.setCancelled(true);
                }
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                event.setCancelled(true);
                if (!clickedInventory.equals(inventory)) {
                    List<Integer> unlockedSlots = slotLockMap.entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue())
                        .map(Map.Entry::getKey)
                        .sorted()
                        .toList();

                    ItemStack clickedItem = event.getCurrentItem();
                    if (clickedItem != null) {
                        int remainingToDistribute = clickedItem.getAmount();
                        int backupDestinationSlot = -1;
                        boolean complete = false;
                        for (int unlockedSlot : unlockedSlots) {
                            if (complete) {
                                break;
                            }

                            ItemStack slotItem = inventory.getItem(unlockedSlot);
                            if ((slotItem == null || slotItem.getType() == Material.AIR) && backupDestinationSlot == -1) {
                                backupDestinationSlot = unlockedSlot;
                                continue;
                            }

                            if (slotItem != null && slotItem.isSimilar(clickedItem)) {
                                int spaceInStack = slotItem.getMaxStackSize() - slotItem.getAmount();

                                if (spaceInStack <= remainingToDistribute) {
                                    slotItem.setAmount(slotItem.getAmount() + remainingToDistribute);
                                    clickedItem.setType(Material.AIR);
                                    complete = true;
                                } else if (spaceInStack > 0) {
                                    remainingToDistribute -= spaceInStack;
                                    slotItem.setAmount(slotItem.getMaxStackSize());
                                    clickedItem.setAmount(clickedItem.getAmount() - spaceInStack);
                                }
                            }
                        }

                        if (!complete && backupDestinationSlot != -1) {
                            inventory.setItem(backupDestinationSlot, clickedItem);
                            event.getInventory().setItem(event.getSlot(), null);
                        }
                    }
                }
            }
        }
    }

    public void onDrag(InventoryDragEvent event) {
        for (int slot : event.getRawSlots()) {
            if (slot <= 53 && this.getSlot(slot).locked()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void onClose(Gui gui, InventoryCloseEvent event, GuiHandler instance) {

        LushLib.getInstance().getPlugin().getManager(GuiManager.class).ifPresent(guiManager -> guiManager.removeGui(player.getUniqueId()));
    }

    public void close() {
        player.closeInventory();
    }

    public static class Builder {
        private final InventoryType inventoryType;
        private final int size;

        private Builder(InventoryType inventoryType) {
            this.inventoryType = inventoryType;
            this.size = inventoryType.getDefaultSize();
        }

        private Builder(int size) {
            this.inventoryType = InventoryType.CHEST;
            this.size = size;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Gui open(Player player) {

        }
    }
}
