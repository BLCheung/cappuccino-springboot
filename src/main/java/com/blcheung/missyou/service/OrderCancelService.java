package com.blcheung.missyou.service;

import com.blcheung.missyou.bo.OrderMessageQueueBO;
import com.blcheung.missyou.constant.Macro;
import com.blcheung.missyou.core.enumeration.OrderStatus;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.exception.http.ServerErrorException;
import com.blcheung.missyou.kit.LocalUserKit;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.model.UserCoupon;
import com.blcheung.missyou.repository.OrderRepository;
import com.blcheung.missyou.repository.SkuRepository;
import com.blcheung.missyou.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BLCheung
 * @date 2021/11/12 4:40 上午
 */
@Service
public class OrderCancelService {
    @Autowired
    private OrderRepository      orderRepository;
    @Autowired
    private SkuRepository        skuRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private CouponBackService    couponBackService;

    /**
     * 取消订单
     *
     * @param oid 订单id
     * @author BLCheung
     * @date 2021/11/16 10:34 下午
     */
    @Transactional
    public void cancel(Long oid) {
        Order order = this.orderRepository.findById(oid)
                                          .orElseThrow(() -> new NotFoundException(70001));

        User user = LocalUserKit.getUser();
        if (!user.getId()
                 .equals(order.getUserId())) throw new ForbiddenException(20012);

        this.cancel(order);

        List<UserCoupon> userCouponList = this.userCouponRepository.findAllByUserIdAndOrderId(user.getId(),
                                                                                              order.getId());
        if (userCouponList.isEmpty()) return;
        List<Long> couponIds = userCouponList.stream()
                                             .map(UserCoupon::getCouponId)
                                             .collect(Collectors.toList());
        this.couponBackService.returnBack(user.getId(), couponIds);
    }

    /**
     * 取消订单
     *
     * @param messageBO redis的订单消息
     * @author BLCheung
     * @date 2021/11/12 4:49 上午
     */
    @Transactional
    public void cancel(OrderMessageQueueBO messageBO) {
        Long orderId = messageBO.getOrderId();

        if (orderId <= 0) throw new ParameterException(70000);

        Order order = this.orderRepository.findById(orderId)
                                          .orElseThrow(() -> new ServerErrorException(70001));

        this.cancel(order);
        this.couponBackService.returnBack(messageBO.getUserId(), messageBO.getCouponIds());
    }


    private void cancel(Order order) {
        if (!OrderStatus.UNPAID.getValue()
                               .equals(order.getStatus())) throw new ForbiddenException(70014);

        int result = this.orderRepository.cancelOrder(order.getId());
        if (result != Macro.OK) ResultKit.reject(70000, "id为" + order.getId() + "的订单取消失败");

        // 归还库存
        order.getSnapItems()
             .stream()
             .filter(skuOrder -> Macro.OK != this.skuRepository.recoverStock(skuOrder.getId(), skuOrder.getCount()))
             .findFirst()
             .ifPresent(skuOrder -> ResultKit.reject(70000, "id为" + skuOrder.getId() + "的商品库存回滚错误,订单取消失败"));
    }
}
