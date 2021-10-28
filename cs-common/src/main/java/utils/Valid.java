package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Ren YuQi
 * @Create 2021-10-28 14:04
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {

    String pattern() default "";

    String tipMsg() default "";

    String columnName() default "";
}
