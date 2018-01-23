package com.github.spardarus.minispring.context.annotations;

import java.lang.annotation.*;

/**
 * This annotation works in
 * conjunction with the annotation @Autowired.
 * In order to specify the bean's name.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    String value() default "";
}
