package com.blcheung.missyou.service;

import com.blcheung.missyou.model.User;
import com.blcheung.missyou.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long uid) { return this.userRepository.findFirstById(uid); }
}
