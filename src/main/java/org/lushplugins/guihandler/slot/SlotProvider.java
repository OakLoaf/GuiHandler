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

        public BasicSlotProvider build() {
            return new BasicSlotProvider(this.iconProvider, this.button);
        }
    }
}
