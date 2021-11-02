package com.blcheung.missyou.service;

import com.blcheung.missyou.constant.Macro;
import com.blcheung.missyou.core.enumeration.OrderStatus;
import com.blcheung.missyou.core.payment.WechatMinPayConfig;
import com.blcheung.missyou.dto.BasePayDTO;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ServerErrorException;
import com.blcheung.missyou.kit.LocalUserKit;
import com.blcheung.missyou.kit.WechatPayKit;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.repository.OrderRepository;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

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

    public Boolean processNotifyXml(String xml) {
        Map<String, String> data;
        try {
            data = WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        WXPay wxPay = WechatPayKit.initWxPay(this.wechatMinPayConfig);

        boolean isValid;
        try {
            isValid = wxPay.isResponseSignatureValid(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!isValid) return false;

        if (!WechatPayKit.isPayResultNotifyValid(data)) return false;

        String orderNo = data.get("out_trade_no");
        return this.dealOrder(orderNo);
    }

    private Boolean dealOrder(String orderNo) {
        Optional<Order> orderOptional = this.orderRepository.findFirstByOrderNo(orderNo);
        if (!orderOptional.isPresent()) return false;

        Order order = orderOptional.get();
        OrderStatus currentStatus = order.getStatusEnum();

        int result = Macro.Fail;
        if (currentStatus.equals(OrderStatus.UNPAID) || currentStatus.equals(OrderStatus.CANCELED)) {
            result = this.orderRepository.orderPaySuccess(order.getId(), new Date());
        } else if (currentStatus.equals(OrderStatus.PAID)) {
            return true;
        }

        return result == Macro.OK;
    }
}
