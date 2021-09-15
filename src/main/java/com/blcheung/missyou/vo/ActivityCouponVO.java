package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Activity;
import com.blcheung.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActivityCouponVO extends ActivityVO {
    private List<Coupon> couponList;

    public ActivityCouponVO(Activity activity) {
        super(activity);
        this.couponList = activity.getCouponList();
    }

}
