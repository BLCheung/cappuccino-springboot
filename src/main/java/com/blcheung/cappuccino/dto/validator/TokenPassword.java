package com.blcheung.cappuccino.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
@Documented
@Constraint(validatedBy = TokenPasswordValidator.class)
public @interface TokenPassword {
    String message() default "{token.password}";

    int min() default 6;

    int max() default 32;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
