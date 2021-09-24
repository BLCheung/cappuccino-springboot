package com.blcheung.missyou.service;

import com.blcheung.missyou.core.enumeration.CouponStatus;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.kit.LocalUserKit;
import com.blcheung.missyou.model.Activity;
import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.model.UserCoupon;
import com.blcheung.missyou.repository.ActivityRepository;
import com.blcheung.missyou.repository.CouponRepository;
import com.blcheung.missyou.repository.UserCouponRepository;
import com.blcheung.missyou.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository     couponRepository;
    @Autowired
    private ActivityRepository   activityRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getCouponByCategoryId(Long cid) {
        return this.couponRepository.findCouponByCategoryId(cid, new Date());
    }

    public List<Coupon> getCouponByIsWholeStore(Boolean isWholeStore) {
        return this.couponRepository.findCouponByIsWholeStore(isWholeStore, new Date());
    }

    public void receiveCoupon(Long couponId) {
        // 优惠券存不存在
        this.couponRepository.findById(couponId)
                             .orElseThrow(() -> new NotFoundException(50001));

        // 该优惠券对应的活动是否已结束
        Date now = new Date();
        Activity activity = this.activityRepository.findByCouponListId(couponId);
        Boolean isInRangeDate = CommonUtils.isInRangeDate(now, activity.getStartTime(), activity.getEndTime());
        if (!isInRangeDate) throw new ParameterException(50002);

        // 该用户是否已领取过
        User user = LocalUserKit.getUser();
        this.userCouponRepository.findFirstByUserIdAndCouponId(user.getId(), couponId)
                                 .ifPresent((uc) -> {throw new ParameterException(50003);});

        // 构建&写入
        UserCoupon userCoupon = UserCoupon.builder()
                                          .userId(user.getId())
                                          .couponId(couponId)
                                          .createTime(now)
                                          .status(CouponStatus.AVAILABLE.getValue())
                                          .build();
        this.userCouponRepository.save(userCoupon);
    }
}
