package org.lushplugins.guihandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Slots {
    /**
     * @return label
     */
    char[] value() default DEFAULT_LABEL;

    char DEFAULT_LABEL = '\n';
}
