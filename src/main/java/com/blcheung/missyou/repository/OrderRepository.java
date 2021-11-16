package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(Long oid);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long uid, Integer status, Pageable pageable);

    Page<Order> findByUserIdAndStatusAndExpiredTimeGreaterThan(Long uid, Integer status, Date now, Pageable pageable);

    Optional<Order> findFirstByUserIdAndId(Long uid, Long oid);

    Optional<Order> findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status = :status\n" + "where o.id = :oid")
    int updateOrderStatus(Long oid, Integer status);

    @Modifying
    @Query("update Order o set o.status = 5\n" + "where o.status = 1\n" + "and o.id = :oid")
    int cancelOrder(Long oid);

    @Modifying
    @Query("update Order o set o.status = 2,\n" + "o.payTime = :payTime\n" + "where o.id = :oid")
    int orderPaySuccess(Long oid, Date payTime);

    @Modifying
    @Query("update Order o\n" + "set o.status = 2,\n" + "o.payTime = :payTime,\n" + "o.payInExpired = true\n" +
           "where o.id = :oid")
    int orderPaySuccessInExpired(Long oid, Date payTime);
}
