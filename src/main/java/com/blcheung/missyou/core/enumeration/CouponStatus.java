package com.blcheung.missyou.core.enumeration;

public enum CouponStatus {
    AVAILABLE(1, "可使用，未过期"),
    USED(2, "已使用"),
    EXPIRED(0, "未使用，已过期");

    private Integer value;

    CouponStatus(Integer value, String desc) {
        this.value = value;
    }

    public Integer getValue() { return this.value; }

    // public static Integer toValue(CouponStatus couponStatus) {
    //     return Stream.of(CouponStatus.values())
    //                  .filter(couponStatusItem -> couponStatus.name()
    //                                                          .equals(couponStatusItem.name()))
    //                  .findAny()
    //                  .map(CouponStatus::getValue)
    //                  .orElse(null);
    // }
}
