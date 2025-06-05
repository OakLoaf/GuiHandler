package org.lushplugins.guihandler.gui;

import com.google.common.collect.TreeMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.ModernChatColorHandler;
import org.lushplugins.guihandler.GuiHandler;
import org.lushplugins.guihandler.slot.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Gui {
    private final GuiHandler instance;
    private final Inventory inventory;
    private final Slot[] slots;
    private final GuiActor actor;
    private final boolean locked;
    private final Map<Character, LabelledSlotProvider> labelProviders;
    private final Map<Class<?>, Object> provided;
    // Consider replacing with a GuiData class to allow users to add custom data fields
    private int page = 1;

    private Gui(GuiHandler instance, Inventory inventory, Slot[] slots, GuiActor actor, boolean locked, Map<Character, LabelledSlotProvider> labelProviders, Map<Class<?>, Object> provided) {
        this.instance = instance;
        this.inventory = inventory;
        this.slots = slots;
        this.actor = actor;
        this.locked = locked;
        this.labelProviders = labelProviders;
        this.provided = provided;

        refreshLabelIndexes();
        open();
    }

    public Inventory inventory() {
        return inventory;
    }

    public GuiActor actor() {
        return actor;
    }

    public Slot[] slots() {
        return Arrays.copyOf(this.slots, this.slots.length);
    }

    // TODO: Ensure it's not possible for the slot array to contain `null`
    public Slot slot(int slot) {
        return this.slots[slot];
    }

    public <T> @Nullable T provided(Class<T> typeClass) {
        Object providedObject = this.provided.get(typeClass);
        //noinspection unchecked
        return providedObject != null ? (T) providedObject : null;
    }

    private void refreshLabelIndexes() {
        Map<Character, AtomicInteger> indexes = new HashMap<>();

        for (Slot slot : this.slots) {
            char label = slot.label();
            int index = indexes.computeIfAbsent(label, ignored -> new AtomicInteger()).getAndIncrement();
            slot.labelIndex(index);
        }
    }

    public void setProviderFor(char label, LabelledSlotProvider provider) {
        this.labelProviders.put(label, provider);
    }

    public void applyLayer(GuiLayer layer) {
        TreeMultimap<Character, Integer> slotMap = layer.getSlotMap();
        for (char label : slotMap.keySet()) {
            SlotProvider provider = Optional.ofNullable(layer.getSlotProvider(label))
                .orElseGet(() -> this.instance.getDefaultProvider(label));
            if (provider == null) {
                continue;
            }

            for (int rawSlot : slotMap.get(label)) {
                Slot slot = this.slot(rawSlot);
                slot.label(label);
                slot.slotProvider(provider);
            }
        }

        refreshLabelIndexes();
    }

    public int page() {
        return this.page;
    }

    public void page(int page) {
        if (this.page != page) {
            this.page = page;
            refresh();
        }
    }

    public void nextPage() {
        this.page(this.page + 1);
    }

    public void previousPage() {
        this.page(this.page - 1);
    }

    protected void open() {
        refresh();

        Player player = this.actor.player();
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this.instance.getPlugin(), () -> player.openInventory(this.inventory));
        } else {
            player.openInventory(this.inventory);
        }

        this.instance.setOpenGui(player.getUniqueId(), this);
    }

    public void refresh(Slot slot) {
        this.inventory.setItem(slot.rawSlot(), slot.icon(this));
    }

    public void refresh() {
        Arrays.stream(this.slots)
            .collect(Collectors.groupingBy(Slot::label))
            .forEach((label, slots) -> {
                LabelledSlotProvider provider = this.labelProviders.get(label);
                if (provider != null) {
                    provider.apply(this, label, slots);
                }
            });

        for (Slot slot : this.slots) {
            refresh(slot);
        }
    }

    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        int rawSlot = event.getRawSlot();
        Slot slot = this.slot(rawSlot);
        if (slot != null) {
            try {
                slot.click(event, this);
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
            if (rawSlot <= 53 && this.slot(rawSlot).locked()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void close() {
        this.actor.player().closeInventory();
    }

    public void onClose(InventoryCloseEvent event) {}

    public static Builder builder(GuiHandler instance) {
        return new Builder(instance);
    }

    public static class Builder {
        private final GuiHandler instance;
        private InventoryType inventoryType = InventoryType.CHEST;
        private int size = 27;
        private String title;
        private boolean locked = false;
        private final List<Character> slots = new ArrayList<>();
        private final Map<Character, SlotProvider> providers = new HashMap<>();
        private final Map<Character, LabelledSlotProvider> labelProviders = new HashMap<>();

        private Builder(GuiHandler instance) {
            this.instance = instance;
        }

        public Builder inventoryType(InventoryType inventoryType) {
            this.inventoryType = inventoryType;
            this.size = inventoryType.getDefaultSize();
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder slot(int slot, char label) {
            // Expands list of slots
            while (this.slots.size() <= slot) {
                this.slots.add(null);
            }

            this.slots.set(slot, label);
            return this;
        }

        public Builder applyLayer(GuiLayer layer) {
            layer.getSlotMap().forEach((label, slot) -> this.slot(slot, label));
            layer.getSlotProviders().forEach(this::setSlotProviderFor);
            return this;
        }

        public Builder setSlotProviderFor(char label, SlotProvider provider) {
            this.providers.put(label, provider);
            return this;
        }

        public Builder setIconProviderFor(char label, IconProvider provider) {
            if (this.providers.containsKey(label)) {
                this.providers.get(label).setIconProvider(provider);
            } else {
                this.providers.put(label, SlotProvider.create(provider));
            }

            return this;
        }

        public Builder setButtonFor(char label, Button button) {
            if (this.providers.containsKey(label)) {
                this.providers.get(label).setButton(button);
            } else {
                this.providers.put(label, SlotProvider.create(button));
            }

            return this;
        }

        public Builder setSlotProviderFor(char label, LabelledSlotProvider provider) {
            this.labelProviders.put(label, provider);
            return this;
        }

        public Gui open(Player player, Object... provided) {
            Inventory inventory;
            Slot[] slots;
            if (this.inventoryType == InventoryType.CHEST) {
                inventory = Bukkit.getServer().createInventory(null, this.size, ModernChatColorHandler.translate(this.title, player));
                slots = new Slot[this.size];
            } else {
                inventory = Bukkit.getServer().createInventory(null, this.inventoryType, ModernChatColorHandler.translate(this.title, player));
                slots = new Slot[this.inventoryType.getDefaultSize()];
            }

            Map<Character, AtomicInteger> indexes = new HashMap<>();
            for (int rawSlot = 0; rawSlot < slots.length; rawSlot++) {
                Character label = Optional.ofNullable(this.slots.get(rawSlot)).orElse(' ');
                int labelIndex = indexes.computeIfAbsent(label, ignored -> new AtomicInteger()).getAndIncrement();
                Slot slot = new Slot(rawSlot, label, labelIndex);

                SlotProvider provider = Optional.ofNullable(this.providers.get(label))
                    .orElseGet(() -> this.instance.getDefaultProvider(label));
                if (provider != null) {
                    slot.slotProvider(provider);
                }

                slots[rawSlot] = slot;
            }

            Map<Class<?>, Object> providedMap = Stream.of(provided)
                .collect(Collectors.toMap(
                    Object::getClass,
                    obj -> obj,
                    (existing, replacement) -> existing
                ));

            return new Gui(this.instance, inventory, slots, new GuiActor(player), this.locked, this.labelProviders, providedMap);
        }
    }
}
