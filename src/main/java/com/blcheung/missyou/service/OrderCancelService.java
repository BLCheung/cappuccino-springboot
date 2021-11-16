package com.blcheung.missyou.service;

import com.blcheung.missyou.bo.OrderRedisMessageBO;
import com.blcheung.missyou.constant.Macro;
import com.blcheung.missyou.core.enumeration.OrderStatus;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.repository.OrderRepository;
import com.blcheung.missyou.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author BLCheung
 * @date 2021/11/12 4:40 上午
 */
@Service
public class OrderCancelService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SkuRepository   skuRepository;

    /**
     * 取消订单
     *
     * @param messageBO redis的订单消息
     * @author BLCheung
     * @date 2021/11/12 4:49 上午
     */
    public void cancel(OrderRedisMessageBO messageBO) {
        Long orderId = messageBO.getOrderId();

        if (orderId <= 0) throw new ParameterException(70000);
        this.cancel(orderId);
    }

    /**
     * 取消订单
     *
     * @param oid 订单id
     * @return boolean
     * @author BLCheung
     * @date 2021/11/16 9:55 下午
     */
    @Transactional
    public boolean cancel(Long oid) {
        Order order = this.orderRepository.findById(oid)
                                          .orElseThrow(() -> new NotFoundException(70001));

        if (!OrderStatus.UNPAID.getValue()
                               .equals(order.getStatus())) throw new ForbiddenException(70014);

        int result = this.orderRepository.cancelOrder(order.getId());
        if (result != Macro.OK) throw new ForbiddenException(70000);

        // 归还库存
        return order.getSnapItems()
                    .stream()
                    .allMatch(skuOrder -> Macro.OK ==
                                          this.skuRepository.recoverStock(skuOrder.getId(), skuOrder.getCount()));
    }
}
