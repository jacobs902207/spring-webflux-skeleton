package project.spring.skeleton.internal.api.common;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiVersion {
    String value() default "";
}