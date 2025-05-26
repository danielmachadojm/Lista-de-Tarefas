package todoapp.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface TaskProperty {
    String description() default "";
    boolean required() default false;
}