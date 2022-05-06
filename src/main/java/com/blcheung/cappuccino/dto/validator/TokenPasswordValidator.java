package com.blcheung.cappuccino.dto.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> { // A: 关联的注解，T: 被校验的字段类型
    private int min;
    private int max;

    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 如果没有密码，让它通过，小程序没有密码
        if (StringUtils.isEmpty(s)) return true;

        return s.length() >= this.min && s.length() <= this.max;
    }
}
