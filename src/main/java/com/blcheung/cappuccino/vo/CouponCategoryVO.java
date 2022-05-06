package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CouponCategoryVO extends CouponVO {
    private List<CategoryVO> categories;

    public CouponCategoryVO(Coupon coupon) {
        super(coupon);
        this.categories = coupon.getCategoryList()
                                .stream()
                                .map(CategoryVO::new)
                                .collect(Collectors.toList());
    }

    public static List<CouponCategoryVO> buildCouponCategoryList(List<Coupon> couponList) {
        if (couponList.isEmpty()) return Collections.emptyList();

        return couponList.stream()
                         .map(CouponCategoryVO::new)
                         .collect(Collectors.toList());
    }
}
