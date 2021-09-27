package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("select c from Coupon c\n" + "join c.categoryList ca\n" + "join Activity a\n" + "on a.id = c.activityId\n" +
           "where ca.id = :cid\n" + "and :date < a.endTime\n" + "and :date > a.startTime")
    List<Coupon> findCouponByCategoryId(Long cid, Date date);

    @Query("select c from Coupon c\n" + "join Activity a\n" + "on a.id = c.activityId\n" +
           "where c.wholeStore = :isWholeStore\n" + "and a.startTime < :date \n" + "and a.endTime > :date")
    List<Coupon> findCouponByIsWholeStore(Boolean isWholeStore, Date date);

    @Query("select c from Coupon c\n" + "join UserCoupon uc\n" + "on c.id = uc.couponId\n" + "join User u\n" +
           "on u.id = uc.userId\n" + "where u.id = :uid\n" + "and uc.status = 0\n" + "and uc.orderId is null\n" +
           "and c.startTime < :now\n" + "and c.endTime > :now\n" + "\n")
    List<Coupon> findMyAvailableCoupon(Long uid, Date now);

    @Query("select c from Coupon c\n" + "join UserCoupon uc\n" + "on c.id = uc.couponId\n" + "join User u\n" +
           "on u.id = uc.userId\n" + "where u.id = :uid\n" + "and uc.status = 2\n" + "and uc.orderId is not null")
    List<Coupon> findMyUsedCoupon(Long uid);

    @Query("select c from Coupon c\n" + "join UserCoupon uc\n" + "on c.id = uc.couponId\n" + "join User u\n" +
           "on u.id = uc.userId\n" + "where u.id = :uid\n" + "and uc.status <> 2\n" + "and c.endTime < :now")
    List<Coupon> findMyExpiredCoupon(Long uid, Date now);
}
