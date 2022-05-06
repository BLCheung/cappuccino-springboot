package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryCouponVO extends CategoryVO {
    private List<CouponVO> coupons;

    public CategoryCouponVO(Category category) {
        super(category);
        this.coupons = category.getCouponList()
                               .stream()
                               .map(CouponVO::new)
                               .collect(Collectors.toList());
    }
}
