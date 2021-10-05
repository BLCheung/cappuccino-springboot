package com.blcheung.missyou.core.enumeration;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CouponType {
    FULL_MINUS(1, "满减券"), FULL_OFF(2, "满减折扣券"), NO_THRESHOLD(0, "无门槛");

    private Integer value;

    CouponType(Integer value, String desc) {
        this.value = value;
    }

    public static Optional<CouponType> toType(Integer value) {
        return Stream.of(CouponType.values())
                     .filter(couponType -> couponType.getValue()
                                                     .equals(value))
                     .findAny();
    }
}
