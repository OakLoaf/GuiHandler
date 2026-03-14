package org.lushplugins.guihandler.config.slot;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.ModernChatColorHandler;
import org.lushplugins.guihandler.slot.SlotContext;
import org.lushplugins.guihandler.slot.SlotIcon;
import org.lushplugins.lushlib.skullcreator.SkullCreatorAPI;

import java.util.List;

public class IconConfig implements SlotIcon {
    private final Material material;
    private final int amount;
    private final String displayName;
    private final List<String> lore;
    private final Boolean enchantGlow;
    private final Integer customModelData;
    private final String skullTexture;

    public IconConfig(ConfigurationSection config) {
        this.material = Registry.MATERIAL.get(Key.key(config.getString("material")));
        this.amount = config.getInt("amount", 1);
        this.displayName = config.getString("display-name");
        this.lore = config.getStringList("lore");
        this.enchantGlow = config.contains("enchant-glow") ? config.getBoolean("enchant-glow") : null;
        this.customModelData = config.contains("custom-model-data") ? config.getInt("custom-model-data") : null;
        this.skullTexture = config.getString("skull-texture");
    }

    public ItemStack asItemStack(@Nullable Player player) {
        ItemStack item = ItemStack.of(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.displayName(ModernChatColorHandler.translate(displayName, player));
            }

            itemMeta.lore(ModernChatColorHandler.translate(lore, player));

            if (enchantGlow != null) {
                itemMeta.setEnchantmentGlintOverride(enchantGlow);
            }

            if (customModelData != null) {
                itemMeta.setCustomModelData(customModelData);
            }

            if (skullTexture != null && itemMeta instanceof SkullMeta skullMeta) {
                if (skullTexture.equals("mirror") && player != null) {
                    String playerB64 = SkullCreatorAPI.getTexture(player);
                    if (playerB64 != null) {
                        SkullCreatorAPI.mutateItemMeta(skullMeta, playerB64);
                    }
                } else {
                    SkullCreatorAPI.mutateItemMeta(skullMeta, skullTexture);
                }
            }

            item.setItemMeta(itemMeta);
        }

        return item;
    }

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return asItemStack(context.gui().actor().player());
    }
}
