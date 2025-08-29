package org.lushplugins.guihandler.gui;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PagedGui<T> {

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
}
