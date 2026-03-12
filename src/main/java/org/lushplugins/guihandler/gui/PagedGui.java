package org.lushplugins.guihandler.gui;

import org.bukkit.inventory.ItemStack;
import org.lushplugins.guihandler.annotation.ButtonProvider;
import org.lushplugins.guihandler.annotation.IconProvider;
import org.lushplugins.guihandler.slot.Slot;
import org.lushplugins.guihandler.slot.SlotContext;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PagedGui<T> {

    char getContentLabel();

    Stream<T> getContentStream(Gui gui);

    Comparator<T> getContentSortMethod();

    default Stream<T> getPageContentStream(Gui gui, int page, int pageSize) {
        return getContentStream(gui)
            .sorted(this.getContentSortMethod())
            .skip((long) (page - 1) * pageSize)
            .limit(pageSize);
    }

    default ArrayDeque<T> getPageContent(Gui gui, int page, int pageSize) {
        return getPageContentStream(gui, page, pageSize).collect(Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * @param context context for where the item will be used
     * @param active whether the previous page button can currently be used
     */
    ItemStack getNextPageIcon(SlotContext context, boolean active);

    /**
     * @param context context for where the item will be used
     * @param active whether the previous page button can currently be used
     */
    ItemStack getPreviousPageIcon(SlotContext context, boolean active);

    @ButtonProvider('>')
    default void nextPageButton(Gui gui) {
        List<Slot> slots = gui.slots(slot -> slot.label() == getContentLabel());
        int pageSize = slots.size();

        Stream<T> content = this.getPageContentStream(gui, gui.page(), pageSize);
        if (content.count() == pageSize) {
            gui.nextPage();
        }
    }

    @IconProvider('>')
    default ItemStack nextPageButtonIcon(SlotContext context) {
        Gui gui = context.gui();
        List<Slot> slots = gui.slots(slot -> slot.label() == getContentLabel());
        int pageSize = slots.size();

        Stream<T> content = this.getPageContentStream(gui, gui.page(), pageSize);
        return getNextPageIcon(context, content.count() == pageSize);
    }

    @ButtonProvider('<')
    default void prevPageButton(Gui gui) {
        if (gui.page() > 1) {
            gui.previousPage();
        }
    }

    @IconProvider('<')
    default ItemStack prevPageButtonIcon(SlotContext context) {
        return getPreviousPageIcon(context, context.gui().page() > 1);
    }
}
