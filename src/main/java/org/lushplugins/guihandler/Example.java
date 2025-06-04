package org.lushplugins.guihandler;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.guihandler.slot.Slot;
import org.lushplugins.guihandler.slot.SlotProvider;

import java.util.ArrayDeque;
import java.util.Deque;

public class Example {

    public void init(JavaPlugin plugin) {
        GuiHandler guiHandler = GuiHandler.builder(plugin)
            .registerLabelProvider(' ', SlotProvider.create(() -> new ItemStack(Material.AIR)))
            .build();

        guiHandler.guiBuilder(0)
            .title("Example Gui")
            .locked(true)
            .setProviderFor('#', SlotProvider.create(new ItemStack(Material.GLASS_PANE)))
            .setProviderFor('F', (gui, label, slots) -> {
                Deque<ItemStack> queue = new ArrayDeque<>();

                for (Slot slot : slots) {
                    slot.icon(queue.pop());
                }
            })
            .open(null);
    }
}
