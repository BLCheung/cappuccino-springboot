package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select c from Coupon c\n" + "join c.categoryList ca\n" + "where ca.id = :cid")
    List<Coupon> findCouponByCategoryId(Long cid);
}
