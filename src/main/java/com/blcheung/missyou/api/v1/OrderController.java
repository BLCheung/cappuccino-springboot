package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.annotations.ScopeLevel;
import com.blcheung.missyou.dto.CreateOrderDTO;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.UserAddress;
import com.blcheung.missyou.service.OrderService;
import com.blcheung.missyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService  userService;


    @PostMapping("/create")
    public void createOrder(@RequestBody @Validated CreateOrderDTO orderDTO) {
        this.orderService.isOK(orderDTO);
    }

    /**
     * 获取用户下单时的收货地址
     *
     * @return
     */
    @GetMapping("/address")
    @ScopeLevel()
    public Result<UserAddress> getUserOrderAddress() {
        List<UserAddress> addressList = this.userService.getUserAddressList();

        if (addressList.isEmpty()) return ResultKit.resolve(null);

        // 过滤出默认地址，如果没有则选最新的收货地址
        UserAddress userAddress = addressList.stream()
                                             .filter(UserAddress::getIsDefault)
                                             .findFirst()
                                             .orElse(addressList.get(0));

        return ResultKit.resolve(userAddress);
    }

}
