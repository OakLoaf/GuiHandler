package org.lushplugins.guihandler.config;

import com.google.common.collect.TreeMultimap;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.config.slot.SlotConfig;
import org.lushplugins.guihandler.gui.Gui;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiConfig {
    private final @Nullable String title;
    private final List<String> format;
    private final Map<Character, SlotConfig> slots;

    public GuiConfig(ConfigurationSection config) {
        this.title = config.getString("title");
        this.format = config.getStringList("format");

        if (this.format.stream().anyMatch(row -> row.length() != 9)) {
            throw new IllegalArgumentException("Rows should be 9 characters long");
        }

        ConfigurationSection slotsSection = config.getConfigurationSection("slots");
        this.slots = slotsSection != null ? slotsSection.getValues(false).entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().charAt(0),
                entry -> new SlotConfig((ConfigurationSection) entry.getValue())
            )) : null;
    }

    public String title() {
        return title;
    }

    public List<String> format() {
        return format;
    }

    public Map<Character, SlotConfig> slots() {
        return slots;
    }

    public int rowCount() {
        return format.size();
    }

    public int size() {
        return this.rowCount() * 9;
    }

    public TreeMultimap<Character, Integer> slotMap() {
        TreeMultimap<Character, Integer> slotMap = TreeMultimap.create();
        for (int slot = 0; slot < this.rowCount() * 9; slot++) {
            char character = charAt(slot);
            slotMap.put(character, slot);
        }

        return slotMap;
    }

    public char charAt(int slot) {
        int row = (int) Math.ceil((slot + 1) / 9F) - 1;
        return this.format.get(row).charAt(slot % 9);
    }

    public Gui.Builder applyTo(Gui.Builder builder) {
        builder.title(title);
        builder.size(this.size());
        this.slotMap().forEach((label, slotIndex) -> builder.slot(slotIndex, label));
        slots.forEach((label, slot) -> {
            builder.setIconFor(label, slot.icon());
            builder.setActionFor(label, slot.action());
        });

        return builder;
    }
}
