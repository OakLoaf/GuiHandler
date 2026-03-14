package org.lushplugins.guihandler.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SlotProvider extends SlotIcon, SlotAction {
    SlotProvider EMPTY = SlotProvider.builder().build();

    boolean hasIcon();

    boolean hasAction();

    /**
     * @return a new or modified {@link SlotProvider} with the provided {@link SlotIcon}
     */
    SlotProvider with(@Nullable SlotIcon icon);

    /**
     * @return a new or modified {@link SlotProvider} with the provided {@link SlotAction}
     */
    SlotProvider with(@Nullable SlotAction action);

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private SlotIcon icon;
        private SlotAction action;

        private Builder() {}

        public Builder icon(@Nullable SlotIcon icon) {
            this.icon = icon;
            return this;
        }

        public Builder icon(@Nullable ItemStack item) {
            return icon((context) -> item);
        }

        public Builder action(@Nullable SlotAction action) {
            this.action = action;
            return this;
        }

        public BasicSlotProvider build() {
            return new BasicSlotProvider(this.icon, this.action);
        }
    }
}
