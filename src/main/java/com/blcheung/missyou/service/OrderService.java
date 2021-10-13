package com.blcheung.missyou.service;

import com.blcheung.missyou.bo.SkuOrderBO;
import com.blcheung.missyou.dto.CreateOrderDTO;
import com.blcheung.missyou.dto.SkuDTO;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.logic.CouponChecker;
import com.blcheung.missyou.logic.OrderChecker;
import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.model.Sku;
import com.blcheung.missyou.model.SkuOrder;
import com.blcheung.missyou.repository.SkuRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private CouponService couponService;
    @Autowired
    private OrderChecker  orderChecker;
    @Autowired
    private CouponChecker couponChecker;
    @Value("${zbl.order.limit_pay_time}")
    private Long          limitPayTime;

    // 订单sku数据模型对象
    @Getter
    private List<SkuOrder> skuOrderList;

    public void createOrder(CreateOrderDTO orderDTO) {
        // 前端Sku集合
        List<SkuDTO> skuDTOList = orderDTO.getSkuList();
        List<Long> skuIds = skuDTOList.stream()
                                      .map(SkuDTO::getId)
                                      .collect(Collectors.toList());
        // 服务端Sku集合
        List<Sku> serverSkuList = this.skuRepository.findAllByIdIn(skuIds);

        // 是否存在对应Sku
        this.orderChecker.isSkuNotOnSale(serverSkuList.size(), skuIds.size());
        // 订单总价
        BigDecimal finalOrderTotalPrice;

        finalOrderTotalPrice = this.calcOrderTotalPrice(serverSkuList, skuDTOList, orderDTO);
        // 订单总价是否合法
        this.orderChecker.isFinalTotalServerPriceOK(finalOrderTotalPrice);


    }

    /**
     * 获取订单的主图
     *
     * @return
     */
    public String getOrderPrimaryImg() {
        return this.skuOrderList.get(0)
                                .getImg();
    }

    /**
     * 获取订单的主标题
     *
     * @return
     */
    public String getOrderPrimaryTitle() {
        return this.skuOrderList.get(0)
                                .getTitle();
    }


    /**
     * 计算订单总价
     *
     * @param serverSkuList
     * @param skuDTOList
     * @param orderDTO
     * @return
     */
    private BigDecimal calcOrderTotalPrice(List<Sku> serverSkuList, List<SkuDTO> skuDTOList, CreateOrderDTO orderDTO) {
        // 服务端订单总价
        BigDecimal finalTotalServerPrice = new BigDecimal("0");
        // 订单所有sku业务对象集合
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        for (int i = 0; i < serverSkuList.size(); i++) {
            Sku sku = serverSkuList.get(i);
            SkuDTO skuDTO = skuDTOList.get(i);
            // 是否售罄或库存不足
            this.orderChecker.isOutOfStock(sku, skuDTO);
            // 是否超出最大购买限制
            this.orderChecker.isLimitMaxBuy(skuDTO, sku.getLimitBuyCount());

            SkuOrderBO skuOrderBO = new SkuOrderBO(sku, skuDTO);
            SkuOrder skuOrder = new SkuOrder(sku, skuOrderBO);
            skuOrderBOList.add(skuOrderBO);
            this.skuOrderList.add(skuOrder);

            finalTotalServerPrice = finalTotalServerPrice.add(skuOrderBO.getTotalPrice());
        }

        if (orderDTO.getCouponIds() != null && !orderDTO.getCouponIds()
                                                        .isEmpty()) {
            finalTotalServerPrice = this.calcOrderCouponTotalPrice(orderDTO.getCouponIds(), skuOrderBOList,
                                                                   finalTotalServerPrice);
        }

        return finalTotalServerPrice;
    }

    /**
     * 计算含有优惠券的订单总价
     *
     * @param couponIds
     * @param skuOrderBOList
     * @param finalTotalServerPrice
     * @return
     */
    private BigDecimal calcOrderCouponTotalPrice(List<Long> couponIds, List<SkuOrderBO> skuOrderBOList,
                                                 BigDecimal finalTotalServerPrice) {
        // 最终经过优惠的订单总价
        BigDecimal finalOrderCouponTotalPrice = null;
        // 当前用户是否拥有某张优惠券
        List<Coupon> userCouponList = this.couponService.getUserAllAvailableCouponIn(couponIds);
        if (userCouponList.isEmpty()) throw new NotFoundException(20011);
        if (couponIds.size() != userCouponList.size()) throw new NotFoundException(20011);

        this.couponChecker.hasSameCategory(userCouponList);
        this.couponChecker.isExpired(userCouponList);

        finalOrderCouponTotalPrice = this.couponChecker.calc(userCouponList, skuOrderBOList, finalTotalServerPrice);

        return finalOrderCouponTotalPrice;
    }
}
