package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.core.annotations.ScopeLevel;
import com.blcheung.cappuccino.dto.CreateOrderDTO;
import com.blcheung.cappuccino.dto.OrderPagingDTO;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.Order;
import com.blcheung.cappuccino.model.UserAddress;
import com.blcheung.cappuccino.service.OrderCancelService;
import com.blcheung.cappuccino.service.OrderService;
import com.blcheung.cappuccino.service.UserService;
import com.blcheung.cappuccino.vo.OrderDetailVO;
import com.blcheung.cappuccino.vo.OrderPagingVO;
import com.blcheung.cappuccino.vo.PagingResultDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService       orderService;
    @Autowired
    private OrderCancelService orderCancelService;
    @Autowired
    private UserService        userService;

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
     * 取消订单
     *
     * @param orderId
     * @return com.blcheung.cappuccino.common.Result<java.lang.Boolean>
     * @author BLCheung
     * @date 2021/11/16 10:39 下午
     */
    @PutMapping("/cancel/{orderId}")
    @ScopeLevel()
    public Result<Boolean> cancelOrder(@PathVariable("orderId") @NotNull(message = "订单id不能为空") Long orderId) {
        this.orderCancelService.cancel(orderId);

        return ResultKit.resolve(true);
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
