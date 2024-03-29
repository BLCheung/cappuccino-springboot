package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户优惠券仓储
 */
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findFirstByUserIdAndCouponId(Long uid, Long cid);

    Optional<UserCoupon> findFirstByUserIdAndCouponIdAndStatus(Long uid, Long cid, Integer status);

    List<UserCoupon> findAllByUserIdAndOrderId(Long uid, Long oid);

    @Modifying
    @Query("update UserCoupon uc\n" +
           "set uc.status = 1,\n" +
           "uc.orderId = :oid\n" +
           "where uc.userId = :uid\n" +
           "and uc.couponId = :cid\n" +
           "and uc.status = 0\n" +
           "and uc.orderId is null\n")
    int writeOff(Long uid, Long cid, Long oid);

    @Modifying
    @Query("update UserCoupon uc\n" +
           "set uc.status = 0,\n" +
           "uc.orderId = null\n" +
           "where uc.couponId in :cids\n" +
           "and uc.userId = :uid\n" + "and uc.orderId is not null\n" + "and uc.status = 1")
    int returnBack(Long uid, List<Long> cids);
}
