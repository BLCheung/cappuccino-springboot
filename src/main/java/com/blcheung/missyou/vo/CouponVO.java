package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Coupon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CouponVO {
    private Long       id;
    private Long       activityId;
    private String     title;
    private Date       startTime;
    private Date       endTime;
    private String     description;
    private BigDecimal fullMoney;
    private BigDecimal minus;
    private BigDecimal rate;
    private Integer    type;
    private String     remark;
    // 是否全场通用优惠券
    private Boolean    wholeStore;

    public CouponVO(Coupon coupon) {
        BeanUtils.copyProperties(coupon, this);
    }

    public static List<CouponVO> buildCouponList(List<Coupon> couponList) {
        if (couponList.isEmpty()) return Collections.emptyList();

        return couponList.stream()
                         .map(CouponVO::new)
                         .collect(Collectors.toList());
    }
}
