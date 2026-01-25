package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;

public interface SlotProvider extends IconProvider, Button {

    /**
     * @return a new or modified {@link SlotProvider} with the provided {@link IconProvider}
     */
    SlotProvider with(IconProvider iconProvider);

    /**
     * @return a new or modified {@link SlotProvider} with the provided {@link Button}
     */
    SlotProvider with(Button button);

    /**
     * @see #builder#iconProvider(IconProvider)
     */
    @Deprecated
    static SlotProvider create(IconProvider provider) {
        return builder()
            .iconProvider(provider)
            .build();
    }

    /**
     * @see #builder#icon(ItemStack)
     */
    @Deprecated
    static SlotProvider create(ItemStack item) {
        return builder()
            .icon(item)
            .build();
    }

    /**
     * @see #builder#button(Button)
     */
    @Deprecated
    static SlotProvider create(Button button) {
        return builder()
            .button(button)
            .build();
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private IconProvider iconProvider = IconProvider.EMPTY;
        private Button button = Button.EMPTY;

        private Builder() {}

        public Builder iconProvider(IconProvider iconProvider) {
            this.iconProvider = iconProvider;
            return this;
        }

        public Builder icon(ItemStack item) {
            return iconProvider((context) -> item);
        }

        public Builder button(Button button) {
            this.button = button;
            return this;
        }

        public SlotProvider build() {
            return new BasicSlotProvider(this.iconProvider, this.button);
        }
    }
}
