package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Activity;
import com.blcheung.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);

    Activity findByCouponListId(Long couponId);
}
