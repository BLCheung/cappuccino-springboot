package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findByIdAndUserId(Long aid, Long uid);

    List<UserAddress> findAllByUserIdOrderByUpdateTimeDesc(Long uid);
}
