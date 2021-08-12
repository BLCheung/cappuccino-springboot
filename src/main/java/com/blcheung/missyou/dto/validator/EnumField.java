package com.blcheung.missyou.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
@Documented
@Constraint(validatedBy = EnumFieldValidator.class)
public @interface EnumField {
    Class<?> value();

    String message() default "{enum.type}";

    boolean canNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
