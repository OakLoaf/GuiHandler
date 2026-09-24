package org.lushplugins.guihandler.util.reflect;

import java.lang.reflect.Method;
import java.util.*;

public class Reflection {

    /**
     * Get all interfaces including super-interfaces
     * @param clazz the class
     * @return set of collected interfaces
     */
    public static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<>();
        Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(clazz);

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            if (current == null) {
                continue;
            }

            for (Class<?> currentInterface : current.getInterfaces()) {
                if (interfaces.add(currentInterface)) {
                    queue.add(currentInterface);
                }
            }

            Class<?> superclass = current.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                queue.add(current.getSuperclass());
            }
        }

        return interfaces;
    }

    /**
     * Get all methods including private and methods from parent classes
     * @param clazz the class
     * @return list of collected methods
     */
    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();

        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            Collections.addAll(methods, current.getDeclaredMethods());

            for (Class<?> interfaceClass : getAllInterfaces(current)) {
                Collections.addAll(methods, interfaceClass.getDeclaredMethods());
            }

            current = current.getSuperclass();
        }

        return methods;
    }
}
