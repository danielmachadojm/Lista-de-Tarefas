package todoapp.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PriorityLevel {
    String[] allowedValues() default {"BAIXA", "MÉDIA", "ALTA"};
}