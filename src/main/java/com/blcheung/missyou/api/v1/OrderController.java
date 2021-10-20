package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.annotations.ScopeLevel;
import com.blcheung.missyou.dto.CreateOrderDTO;
import com.blcheung.missyou.dto.OrderPagingDTO;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.model.UserAddress;
import com.blcheung.missyou.service.OrderService;
import com.blcheung.missyou.service.UserService;
import com.blcheung.missyou.vo.OrderDetailVO;
import com.blcheung.missyou.vo.OrderPagingVO;
import com.blcheung.missyou.vo.PagingResultDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService  userService;

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    @PostMapping("/create")
    @ScopeLevel()
    public Result<Long> createOrder(@RequestBody @Validated CreateOrderDTO orderDTO) {
        Long orderId = this.orderService.createOrder(orderDTO);

        return ResultKit.resolve(orderId);
    }

    /**
     * 订单列表
     *
     * @param orderPagingDTO
     * @return
     */
    @PostMapping("/list")
    @ScopeLevel()
    public Result<PagingResultDozer<Order, OrderPagingVO>> getOrderList(
            @RequestBody @Validated OrderPagingDTO orderPagingDTO) {
        PagingResultDozer<Order, OrderPagingVO> orderList = this.orderService.getOrderList(orderPagingDTO);

        return ResultKit.resolve(orderList);
    }

    /**
     * 获取订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ScopeLevel
    public Result<OrderDetailVO> getOrderDetail(@PathVariable @NotNull(message = "订单id不能为空") Long id) {
        OrderDetailVO orderDetailVO = this.orderService.getOrderDetail(id);

        return ResultKit.resolve(orderDetailVO);
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
