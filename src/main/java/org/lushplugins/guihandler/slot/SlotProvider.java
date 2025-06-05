package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SlotProvider implements IconProvider, Button {
    private IconProvider iconProvider;
    private Button button;

    public SlotProvider() {
        this.iconProvider = IconProvider.empty();
        this.button = Button.empty();
    }

    public SlotProvider(IconProvider iconProvider, Button button) {
        this.iconProvider = iconProvider;
        this.button = button;
    }

    @Override
    public @Nullable ItemStack icon(SlotContext context) {
        return this.iconProvider.icon(context);
    }

    @Override
    public void click(SlotContext context) {
        this.button.click(context);
    }

    public IconProvider getIconProvider() {
        return iconProvider;
    }

    public void setIconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public static SlotProvider create(IconProvider iconProvider) {
        return new SlotProvider(iconProvider, Button.empty());
    }

    public static SlotProvider create(ItemStack item) {
        return create((IconProvider) (context) -> item);
    }

    public static SlotProvider create(Button button) {
        return new SlotProvider(IconProvider.empty(), button);
    }
}
