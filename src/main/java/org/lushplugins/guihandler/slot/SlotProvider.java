package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SlotProvider implements IconProvider, Button {
    private IconProvider iconProvider;
    private Button button;

    public SlotProvider() {
        this.iconProvider = IconProvider.EMPTY;
        this.button = Button.EMPTY;
    }

    /**
     * @see #iconProvider(IconProvider)
     * @see #button(Button)
     */
    @Deprecated
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

    public IconProvider iconProvider() {
        return iconProvider;
    }

    public SlotProvider iconProvider(IconProvider iconProvider) {
        this.iconProvider = iconProvider;
        return this;
    }

    public SlotProvider icon(ItemStack item) {
        return iconProvider((context) -> item);
    }

    public Button button() {
        return button;
    }

    public SlotProvider button(Button button) {
        this.button = button;
        return this;
    }

    /**
     * @see #iconProvider(IconProvider)
     */
    @Deprecated
    public static SlotProvider create(IconProvider provider) {
        return new SlotProvider().iconProvider(provider);
    }

    /**
     * @see #icon(ItemStack)
     */
    @Deprecated
    public static SlotProvider create(ItemStack item) {
        return new SlotProvider().icon(item);
    }

    /**
     * @see #button(Button)
     */
    @Deprecated
    public static SlotProvider create(Button button) {
        return new SlotProvider().button(button);
    }
}
