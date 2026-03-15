package org.lushplugins.guihandler.annotation;

import org.bukkit.event.inventory.InventoryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomGui {
    InventoryType inventoryType() default InventoryType.CHEST;
    int size() default DEFAULT_SIZE;
    String title() default "";

    int DEFAULT_SIZE = 27;
}
