package com.blcheung.cappuccino.repository;

import com.blcheung.cappuccino.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOpenid(String openId);

    User findByEmail(String email);

    User findFirstById(Long id);

    User findByMobile(String mobile);
}
