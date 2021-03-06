package com.blcheung.missyou.validators;

import javax.validation.Payload;
import java.lang.annotation.*;

@Documented // 可以让注解的注释加入到文档内
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface PasswordEqual {
    String message() default "密码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
