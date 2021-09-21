package com.blcheung.missyou.service;

import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getCouponByCategoryId(Long cid) {
        return this.couponRepository.findCouponByCategoryId(cid);
    }
}
