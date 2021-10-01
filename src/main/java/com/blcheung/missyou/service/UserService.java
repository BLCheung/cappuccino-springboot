package com.blcheung.missyou.service;

import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.kit.LocalUserKit;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.model.UserAddress;
import com.blcheung.missyou.repository.UserAddressRepository;
import com.blcheung.missyou.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository        userRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;

    public User getUserById(Long uid) { return this.userRepository.findFirstById(uid); }

    public List<UserAddress> getUserAddress() {
        Long userId = LocalUserKit.getUser()
                                  .getId();

        List<UserAddress> userAddressList = this.userAddressRepository.findAllByUserIdOrderByUpdateTimeDesc(userId);
        if (userAddressList.isEmpty()) return Collections.emptyList();

        return userAddressList;
    }
}
