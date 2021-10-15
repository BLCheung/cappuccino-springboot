package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findFirstByUserIdAndCouponId(Long uid, Long cid);

    Optional<UserCoupon> findFirstByUserIdAndCouponIdAndStatus(Long uid, Long cid, Integer status);

    @Modifying
    @Query("update UserCoupon uc\n" + "set uc.status = 1,\n" + "uc.orderId = :oid\n" + "where uc.userId = :uid\n" +
           "and uc.couponId = :cid\n" + "and uc.status = 0\n" + "and uc.orderId is null\n" + "\n")
    int writeOff(Long uid, Long cid, Long oid);
}
