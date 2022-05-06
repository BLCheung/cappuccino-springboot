package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);

    Activity findByCouponListId(Long couponId);
}
