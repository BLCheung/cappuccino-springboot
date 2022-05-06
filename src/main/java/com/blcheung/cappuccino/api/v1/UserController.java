package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.core.annotations.ScopeLevel;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.UserAddress;
import com.blcheung.cappuccino.service.UserService;
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
        return ResultKit.resolve(this.userService.getUserAddressList());
    }
}
