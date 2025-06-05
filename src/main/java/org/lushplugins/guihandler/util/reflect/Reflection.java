package org.lushplugins.guihandler.util.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reflection {

    /**
     * Gets all methods including private and methods from parent classes
     * @param clazz the class
     * @return list of collected methods
     */
    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();

        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            Collections.addAll(methods, current.getDeclaredMethods());
            current = current.getSuperclass();
        }

        return methods;
    }
}
