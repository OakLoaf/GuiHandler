package org.lushplugins.guihandler.gui.event;

import java.util.*;
import java.util.function.Consumer;

public class GuiListeners {
    private final Map<Class<?>, List<Consumer<? super GuiEvent>>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends GuiEvent> void register(Class<T> eventClass, Consumer<? super T> consumer) {
        listeners.computeIfAbsent(eventClass, ignored -> new ArrayList<>())
            .add((Consumer<? super GuiEvent>) consumer);
    }

    public void unregisterAll() {
        listeners.clear();
    }

    public <T extends GuiEvent> void call(T event) {
        List<Consumer<? super GuiEvent>> listener = this.listeners.get(event.getClass());
        if (listener != null) {
            for (Consumer<? super GuiEvent> consumer : listener) {
                consumer.accept(event);
            }
        }
    }
}

