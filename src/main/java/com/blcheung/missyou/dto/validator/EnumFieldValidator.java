package com.blcheung.missyou.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 枚举校验器
 * 校验枚举是否包含指定的值
 */
public class EnumFieldValidator implements ConstraintValidator<EnumField, Object> {
    private final List<Object> enumConstArr = new ArrayList<>();
    private       boolean      canNull;

    @Override
    public void initialize(EnumField constraintAnnotation) {
        this.canNull = constraintAnnotation.canNull();
        Class<?> clazz = constraintAnnotation.value();
        Object[] enumConstants = clazz.getEnumConstants();
        Arrays.stream(enumConstants)
              .forEach(enumItem -> {
                  enumConstArr.add(enumItem.toString());
              });
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (this.canNull) return true;

        return !Objects.isNull(value) && this.enumConstArr.contains(value);
    }
}
