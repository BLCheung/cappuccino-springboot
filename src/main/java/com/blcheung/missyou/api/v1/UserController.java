package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.annotations.ScopeLevel;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.UserAddress;
import com.blcheung.missyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户收货地址列表
     *
     * @return
     */
    @GetMapping("/address")
    @ScopeLevel()
    private Result<List<UserAddress>> getUserAddress() {
        return ResultKit.resolve(this.userService.getUserAddress());
    }
}
