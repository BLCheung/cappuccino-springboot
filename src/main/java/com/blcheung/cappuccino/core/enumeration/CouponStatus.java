package com.blcheung.cappuccino.core.enumeration;

import java.util.Optional;
import java.util.stream.Stream;

public enum CouponStatus {
    AVAILABLE(0, "可使用，未过期"),
    USED(1, "已使用"),
    EXPIRED(2, "未使用，已过期");

    private Integer value;

    CouponStatus(Integer value, String desc) {
        this.value = value;
    }

    public Integer getValue() { return this.value; }

    public static Optional<CouponStatus> toType(Integer value) {
        return Stream.of(CouponStatus.values())
                     .filter(couponStatus -> couponStatus.getValue()
                                                         .equals(value))
                     .findAny();
    }

    // public static Integer toValue(CouponStatus couponStatus) {
    //     return Stream.of(CouponStatus.values())
    //                  .filter(couponStatusItem -> couponStatus.name()
    //                                                          .equals(couponStatusItem.name()))
    //                  .findAny()
    //                  .map(CouponStatus::getValue)
    //                  .orElse(null);
    // }
}
