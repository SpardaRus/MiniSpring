package com.github.spardarus.minispring.context.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks methods
 * in the configuration file that create bean
 */
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    String name() default "";
}