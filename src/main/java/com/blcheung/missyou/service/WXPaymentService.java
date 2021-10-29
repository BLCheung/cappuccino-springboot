package com.blcheung.missyou.service;

import com.blcheung.missyou.core.payment.WechatMinPayConfig;
import com.blcheung.missyou.dto.BasePayDTO;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.kit.LocalUserKit;
import com.blcheung.missyou.kit.WechatPayKit;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WXPaymentService {
    @Autowired
    private OrderRepository    orderRepository;
    @Autowired
    private OrderService       orderService;
    @Autowired
    private WechatMinPayConfig wechatMinPayConfig;


    public Map<String, String> unifiedOrder(BasePayDTO payDTO) {
        Long orderId = payDTO.getOrderId();
        User user = LocalUserKit.getUser();

        Order order = this.orderRepository.findFirstByUserIdAndId(user.getId(), orderId)
                                          .orElseThrow(() -> new NotFoundException(70001));

        if (!order.canPay()) throw new ForbiddenException(70013);

        Map<String, String> respData = WechatPayKit.unifiedOrderByMin(order);

        String prepayId = respData.get("prepay_id");
        this.orderService.updateOrderPrepayId(order.getId(), prepayId);

        return WechatPayKit.generateMinPaySignature(respData);
    }
}
