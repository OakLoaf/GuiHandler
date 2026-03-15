package org.lushplugins.guihandler.annotation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationList {
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    public AnnotationList(AnnotatedElement element) {
        Map<Class<? extends Annotation>, Annotation> annotations = new LinkedHashMap<>();

        for (Annotation annotation : element.getAnnotations()) {
            annotations.put(annotation.annotationType(), annotation);
        }

        this.annotations = annotations;
    }

    public <T extends Annotation> @Nullable T get(@NotNull Class<T> type) {
        //noinspection unchecked
        return (T) annotations.get(type);
    }

    public boolean contains(Class<? extends Annotation> type) {
        return annotations.containsKey(type);
    }

    public boolean isEmpty() {
        return this.annotations.isEmpty();
    }
}
