package org.lushplugins.guihandler.gui;

import com.google.common.collect.TreeMultimap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.chatcolorhandler.ModernChatColorHandler;
import org.lushplugins.guihandler.slot.ButtonProvider;
import org.lushplugins.guihandler.slot.Slot;

import java.util.Arrays;
import java.util.List;

public class Gui {
    private final Inventory inventory;
    private final Slot[] slots;
    private final Player player;
    private final boolean locked;

    private Gui(JavaPlugin plugin, Inventory inventory, Slot[] slots, Player player, boolean locked) {
        this.inventory = inventory;
        this.slots = slots;
        this.player = player;
        this.locked = locked;

        open(plugin);
    }

    public Gui(JavaPlugin plugin, int size, Component component, Player player, boolean locked) {
        this(
            plugin,
            Bukkit.getServer().createInventory(null, size, component),
            new Slot[size],
            player,
            locked
        );
    }

    public Gui(JavaPlugin plugin, InventoryType inventoryType, Component component, Player player, boolean locked) {
        this(
            plugin,
            Bukkit.getServer().createInventory(null, inventoryType, component),
            new Slot[inventoryType.getDefaultSize()],
            player,
            locked
        );
    }

    public Gui(JavaPlugin plugin, GuiLayer layer, Component title, Player player, boolean locked) {
        this(plugin, layer.getSize(), title, player, locked);
        applyLayer(layer);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public Slot[] getSlots() {
        return slots;
    }

    public Slot getSlot(int slot) {
        return this.slots[slot] != null ? this.slots[slot] : new Slot(slot, ' ');
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

    public void refresh(Slot slot) {
        this.inventory.setItem(slot.rawSlot(), slot.icon());
    }

    public void refresh() {
        for (Slot slot : this.slots) {
            refresh(slot);
        }
    }

    protected void open(JavaPlugin plugin) {
        refresh();

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> this.player.openInventory(this.inventory));
        } else {
            this.player.openInventory(this.inventory);
        }
    }

    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        int rawSlot = event.getRawSlot();
        Slot slot = this.getSlot(rawSlot);
        if (slot != null) {
            try {
                slot.click(new GuiActor(this.player), this);
                slot.click(event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (this.locked) {
            event.setCancelled(true);
            return;
        }

        switch (event.getAction()) {
            case COLLECT_TO_CURSOR -> event.setCancelled(true);
            case DROP_ALL_SLOT, DROP_ONE_SLOT, PLACE_ALL, PLACE_SOME, PLACE_ONE, PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE, SWAP_WITH_CURSOR, CLONE_STACK, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD  -> {
                if (clickedInventory.equals(this.inventory) && this.slots[rawSlot].locked()) {
                    event.setCancelled(true);
                }
            }
            case MOVE_TO_OTHER_INVENTORY -> {
                event.setCancelled(true);

                if (clickedInventory.equals(this.inventory)) {
                    break;
                }

                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem == null) {
                    return;
                }

                List<Integer> unlockedSlots = Arrays.stream(this.slots)
                    .filter(aSlot -> !aSlot.locked())
                    .map(Slot::rawSlot)
                    .sorted()
                    .toList();

                int remainingToDistribute = clickedItem.getAmount();
                int backupDestinationSlot = -1;
                boolean complete = false;
                for (int unlockedSlot : unlockedSlots) {
                    if (complete) {
                        break;
                    }

                    ItemStack slotItem = this.inventory.getItem(unlockedSlot);
                    if (backupDestinationSlot == -1 && (slotItem == null || slotItem.getType() == Material.AIR)) {
                        backupDestinationSlot = unlockedSlot;
                        continue;
                    }

                    if (slotItem != null && slotItem.isSimilar(clickedItem)) {
                        int spaceInStack = slotItem.getMaxStackSize() - slotItem.getAmount();

                        if (spaceInStack <= remainingToDistribute) {
                            slotItem.setAmount(slotItem.getAmount() + remainingToDistribute);
                            event.setCurrentItem(null);
                            complete = true;
                        } else if (spaceInStack > 0) {
                            remainingToDistribute -= spaceInStack;
                            slotItem.setAmount(slotItem.getMaxStackSize());
                            clickedItem.setAmount(clickedItem.getAmount() - spaceInStack);
                        }
                    }
                }

                if (!complete && backupDestinationSlot != -1) {
                    this.inventory.setItem(backupDestinationSlot, clickedItem);
                    event.getInventory().setItem(event.getSlot(), null);
                }
            }
        }
    }

    public void onDrag(InventoryDragEvent event) {
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot <= 53 && this.getSlot(rawSlot).locked()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void close() {
        this.player.closeInventory();
    }

    public void onClose(InventoryCloseEvent event) {}

    public static Builder builder(JavaPlugin plugin, InventoryType inventoryType) {
        return new Builder(plugin, inventoryType);
    }

    public static Builder builder(JavaPlugin plugin, int size) {
        return new Builder(plugin, size);
    }

    // TODO: Add ability to apply a layer and define slots to builder
    public static class Builder {
        private final JavaPlugin plugin;
        private final InventoryType inventoryType;
        private int size;
        private Component title = Component.empty();
        private boolean locked = false;

        private Builder(JavaPlugin plugin, InventoryType inventoryType) {
            this.plugin = plugin;
            this.inventoryType = inventoryType;
            this.size = inventoryType.getDefaultSize();
        }

        private Builder(JavaPlugin plugin, int size) {
            this.plugin = plugin;
            this.inventoryType = InventoryType.CHEST;
            this.size = size;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder title(String title) {
            return this.title(ModernChatColorHandler.translate(title));
        }

        public Builder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Gui open(Player player) {
            if (this.inventoryType == InventoryType.CHEST) {
                return new Gui(this.plugin, this.size, this.title, player, this.locked);
            } else {
                return new Gui(this.plugin, this.inventoryType, this.title, player, this.locked);
            }
        }
    }
}
