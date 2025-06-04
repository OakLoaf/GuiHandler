package org.lushplugins.guihandler.gui;

import com.google.common.collect.TreeMultimap;
import org.lushplugins.guihandler.slot.SlotProvider;

import java.util.*;

public class GuiLayer {
    private final List<String> rows;
    private final Map<Character, SlotProvider> providers = new HashMap<>();

    public GuiLayer(List<String> rows) {
        if (rows.stream().anyMatch(row -> row.length() != 9)) {
            throw new IllegalArgumentException("Rows should be 9 characters long.");
        }

        this.rows = new ArrayList<>(rows);
    }

    public GuiLayer(String... rows) {
        this(Arrays.asList(rows));
    }

    public GuiLayer() {
        this(new ArrayList<>());
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRow(int index, String format) {
        if (format.length() != 9) {
            throw new IllegalArgumentException("Rows should be 9 characters long.");
        }

        this.rows.set(index, format);
    }

    public void addRow(String format) {
        if (format.length() != 9) {
            throw new IllegalArgumentException("All rows should be 9 characters long.");
        }

        this.rows.add(format);
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getSize() {
        return rows.size() * 9;
    }

    public Map<Character, SlotProvider> getSlotProviders() {
        return providers;
    }

    public SlotProvider getSlotProvider(char character) {
        return this.providers.get(character);
    }

    public void setSlotProvider(char character, SlotProvider provider) {
        this.providers.put(character, provider);
    }

    public TreeMultimap<Character, Integer> getSlotMap() {
        TreeMultimap<Character, Integer> slotMap = TreeMultimap.create();
        for (int slot = 0; slot < getRowCount() * 9; slot++) {
            char character = getCharAt(slot);
            slotMap.put(character, slot);
        }

        return slotMap;
    }

    public char getCharAt(int slot) {
        int currRow = (int) Math.ceil((slot + 1) / 9F) - 1;
        int slotInRow = slot % 9;

        return this.rows.get(currRow).charAt(slotInRow);
    }

    public int getCharCount(char character) {
        int count = 0;

        for (String row : this.rows) {
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == character) {
                    count++;
                }
            }
        }

        return count;
    }
}
