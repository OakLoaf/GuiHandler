package org.lushplugins.guihandler.icon;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.guihandler.util.JsonPropertyWithDefault;
import org.lushplugins.lushlib.skullcreator.SkullCreatorAPI;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("FieldMayBeFinal")
public class Icon {

    @JsonPropertyWithDefault
    private Material material = null;
    @JsonPropertyWithDefault
    private int amount = 1;
    @JsonPropertyWithDefault
    private final String displayName = null;
    @JsonPropertyWithDefault
    private final List<String> lore = null;
    @JsonPropertyWithDefault
    private final Boolean enchantGlow = null;
    @JsonPropertyWithDefault
    private final Integer customModelData = null;
    @JsonPropertyWithDefault
    private String skullTexture = null;

    @Nullable
    public Material getType() {
        return material;
    }

    public boolean hasType() {
        return material != null;
    }

    public int getAmount() {
        return amount;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    public boolean hasLore() {
        return lore != null;
    }

    @Nullable
    public Boolean getEnchantGlow() {
        return enchantGlow;
    }

    public boolean hasEnchantGlow() {
        return enchantGlow != null;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemStack asItemStack() {
        return asItemStack(null, ChatColorHandler::translate);
    }

    public ItemStack asItemStack(@Nullable Player player) {
        return asItemStack(player, ChatColorHandler::translate);
    }

    public ItemStack asItemStack(@Nullable Player player, Function<String, String> parser) {
        if (material == null) {
            throw new IllegalArgumentException("A material must be defined");
        }

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (displayName != null) {
                itemMeta.setDisplayName(parser.apply(displayName));
            }

            if (lore != null) {
                itemMeta.setLore(lore.stream().map(parser).toList());
            }

            if (enchantGlow != null && enchantGlow) {
                itemMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (customModelData != 0) {
                itemMeta.setCustomModelData(customModelData);
            }

            if (itemMeta instanceof SkullMeta skullMeta && skullTexture != null) {
                if (skullTexture.equals("mirror") && player != null) {
                    String playerB64 = SkullCreatorAPI.getTexture(player);
                    if (playerB64 != null) {
                        SkullCreatorAPI.mutateItemMeta(skullMeta, playerB64);
                    }
                } else {
                    SkullCreatorAPI.mutateItemMeta(skullMeta, skullTexture);
                }
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
