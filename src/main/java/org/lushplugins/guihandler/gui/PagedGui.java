package org.lushplugins.guihandler.gui;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PagedGui<T> {

    Stream<T> getContentStream(GuiActor actor);

    Comparator<T> getContentSortMethod();

    default Stream<T> getPageContentStream(GuiActor actor, int page, int pageSize) {
        return getContentStream(actor)
            .sorted(this.getContentSortMethod())
            .skip((long) (page - 1) * pageSize)
            .limit(pageSize);
    }

    default ArrayDeque<T> getPageContent(GuiActor actor, int page, int pageSize) {
        return getPageContentStream(actor, page, pageSize).collect(Collectors.toCollection(ArrayDeque::new));
    }
}
