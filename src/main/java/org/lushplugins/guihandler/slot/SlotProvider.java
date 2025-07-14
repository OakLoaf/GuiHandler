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

    /**
     * @see #builder()
     */
    @Deprecated(forRemoval = true)
    public static SlotProvider create(IconProvider iconProvider) {
        return new SlotProvider(iconProvider, Button.EMPTY);
    }

    /**
     * @see #builder()
     */
    @Deprecated(forRemoval = true)
    public static SlotProvider create(ItemStack item) {
        return create((IconProvider) (context) -> item);
    }

    /**
     * @see #builder()
     */
    @Deprecated(forRemoval = true)
    public static SlotProvider create(Button button) {
        return new SlotProvider(IconProvider.EMPTY, button);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private IconProvider iconProvider = IconProvider.EMPTY;
        private Button button = Button.EMPTY;

        private Builder() {}

        public Builder iconProvider(IconProvider iconProvider) {
            this.iconProvider = iconProvider;
            return this;
        }

        public Builder icon(ItemStack item) {
            return this.iconProvider((context) -> item);
        }

        public Builder button(Button button) {
            this.button = button;
            return this;
        }

        public SlotProvider build() {
            return new SlotProvider(this.iconProvider, this.button);
        }
    }
}
