package org.lushplugins.guihandler.config.slot;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.guihandler.slot.SlotContext;
import org.lushplugins.guihandler.slot.SlotIcon;
import org.lushplugins.lushlib.config.YamlConverter;
import org.lushplugins.lushlib.item.DisplayItemStack;

public record IconConfig(DisplayItemStack icon) implements SlotIcon {

    public IconConfig(ConfigurationSection config) {
        this(YamlConverter.getDisplayItem(config));
    }

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return icon.asItemStack(context.gui().actor().player());
    }

    public DisplayItemStack.Builder overwrite(DisplayItemStack.Builder overwrite) {
        return DisplayItemStack.builder(icon).overwrite(overwrite);
    }
}
