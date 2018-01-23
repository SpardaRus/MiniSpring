package com.github.spardarus.minispring.context.annotations;

import java.lang.annotation.*;

/**
 * With this annotation to specify a
 * configuration file other configuration
 * files to take out them bean
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {
    Class<?>[] value();
}