package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActivityCouponVO extends ActivityVO {
    private List<CouponVO> coupons;

    public ActivityCouponVO(Activity activity) {
        super(activity);
        this.coupons = activity.getCouponList()
                               .stream()
                               .map(CouponVO::new)
                               .collect(Collectors.toList());
    }

}
