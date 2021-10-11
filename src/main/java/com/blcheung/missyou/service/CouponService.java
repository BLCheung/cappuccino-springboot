package com.blcheung.missyou.service;

import com.blcheung.missyou.core.enumeration.CouponStatus;
import com.blcheung.missyou.exception.http.ForbiddenException;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取用户所有可用的优惠券
     *
     * @param couponIds 优惠券id集合
     * @return
     */
    public List<Coupon> getUserAllAvailableCouponIn(List<Long> couponIds) {
        // 是否有重复优惠券id
        if (!CommonUtils.isDistinctIds(couponIds)) throw new ForbiddenException(50005);

        User user = LocalUserKit.getUser();
        List<Coupon> myAvailableCoupon = this.couponRepository.findAllMyAvailableCoupon(user.getId(), new Date());

        if (myAvailableCoupon.isEmpty()) return Collections.emptyList();

        // List<Coupon> userCouponList = new ArrayList<>();
        // for (Coupon coupon : myAvailableCoupon) {
        //     for (Long couponId : couponIds) {
        //         if (couponId.equals(coupon.getId())) {
        //             userCouponList.add(coupon);
        //             break;
        //         }
        //     }
        // }

        return myAvailableCoupon.stream()
                                .filter(coupon -> couponIds.stream()
                                                           .anyMatch(couponId -> this.isSameCoupon(coupon, couponId)))
                                .collect(Collectors.toList());
    }

    /**
     * 领取优惠券
     *
     * @param couponId
     */
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

    public List<Coupon> getMyAvailableCoupon() {
        User user = LocalUserKit.getUser();
        return this.couponRepository.findAllMyAvailableCoupon(user.getId(), new Date());
    }

    public List<Coupon> getMyUsedCoupon() {
        User user = LocalUserKit.getUser();
        return this.couponRepository.findAllMyUsedCoupon(user.getId());
    }

    public List<Coupon> getMyExpiredCoupon() {
        User user = LocalUserKit.getUser();
        return this.couponRepository.findAllMyExpiredCoupon(user.getId(), new Date());
    }


    private List<Coupon> getEqualCoupons(List<Coupon> couponList, Long couponId) {
        return couponList.stream()
                         .filter(coupon -> this.isSameCoupon(coupon, couponId))
                         .collect(Collectors.toList());
    }

    /**
     * 是否匹配的优惠券
     *
     * @param coupon
     * @param couponId
     * @return
     */
    private Boolean isSameCoupon(Coupon coupon, Long couponId) {
        return coupon.getId()
                     .equals(couponId);
    }
}
