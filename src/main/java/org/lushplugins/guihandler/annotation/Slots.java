package org.lushplugins.guihandler.annotation;

public @interface Slots {
    /**
     * @return label
     */
    char value() default DEFAULT_LABEL;

    char DEFAULT_LABEL = '\n';
}
