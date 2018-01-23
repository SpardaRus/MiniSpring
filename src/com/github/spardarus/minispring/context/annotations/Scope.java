package com.github.spardarus.minispring.context.annotations;

import java.lang.annotation.*;

/**
 * This annotation choose the option
 * to create a bean.
 * Bean can be in a single instance
 * or always be created new.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
    String value() default "singleton";
}
