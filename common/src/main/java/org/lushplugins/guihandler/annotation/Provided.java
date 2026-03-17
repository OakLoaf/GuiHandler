package org.lushplugins.guihandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Provided {
    String CLASS_KEY = "_object-type";

    /**
     * @return key of provided object
     */
    String value() default CLASS_KEY;
}
