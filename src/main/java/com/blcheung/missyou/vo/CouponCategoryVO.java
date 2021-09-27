package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;

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
}
