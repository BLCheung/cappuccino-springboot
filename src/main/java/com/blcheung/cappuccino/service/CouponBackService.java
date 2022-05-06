package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.constant.Macro;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BLCheung
 * @date 2021/11/12 4:43 上午
 */
@Service
public class CouponBackService {
    @Autowired
    private UserCouponRepository userCouponRepository;

    /**
     * 归还优惠券
     *
     * @param userId
     * @param couponIds
     * @author BLCheung
     * @date 2021/11/17 2:41 上午
     */
    public void returnBack(Long userId, List<Long> couponIds) {
        if (couponIds.isEmpty() || couponIds.contains((long) -1)) return;

        int result = this.userCouponRepository.returnBack(userId, couponIds);
        if (result != Macro.OK) ResultKit.reject(50000, "id" + userId + "用户的" + couponIds + "优惠券归还失败!");
    }

    // public void returnBack(OrderRedisMessageBO messageBO) {
    //     List<Long> couponIds = messageBO.getCouponIds();
    //     Long userId = messageBO.getUserId();
    //     Long orderId = messageBO.getOrderId();
    //
    // }
}
