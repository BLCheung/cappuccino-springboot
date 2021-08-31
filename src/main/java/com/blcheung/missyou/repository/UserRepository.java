package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByOpenid(String openId);

    User findByEmail(String email);

    User findFirstById(Long id);

    User findByMobile(String mobile);
}
