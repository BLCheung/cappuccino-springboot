package com.blcheung.missyou.logic;

import com.blcheung.missyou.bo.SkuOrderBO;
import com.blcheung.missyou.dto.CreateOrderDTO;
import com.blcheung.missyou.dto.SkuDTO;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.model.Sku;
import com.blcheung.missyou.repository.SkuRepository;
import com.blcheung.missyou.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)   // 多例
public class OrderChecker {
    @Autowired
    private CouponService couponService;

    @Autowired
    private SkuRepository skuRepository;
    @Value("${zbl.sku.limit_buy_count}")
    private Long          limitBuyCount;

    public OrderChecker() {
    }

    public void isOK(CreateOrderDTO orderDTO) {
        // 前端Sku集合
        List<SkuDTO> skuDTOList = orderDTO.getSkuList();
        // 前端Sku ID集合
        List<Long> skuIds = skuDTOList.stream()
                                      .map(SkuDTO::getId)
                                      .collect(Collectors.toList());
        // 服务端Sku集合
        List<Sku> serverSkuList = this.skuRepository.findAllByIdIn(skuIds);

        // 是否存在对应Sku
        this.isSkuNotOnSale(skuIds.size(), serverSkuList.size());

        // 服务端订单总价
        BigDecimal finalTotalServerPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        for (int i = 0, length = serverSkuList.size(); i < length; i++) {
            Sku sku = serverSkuList.get(i);
            SkuDTO skuDTO = skuDTOList.get(i);
            // 是否售罄或库存不足
            this.isOutOfStock(sku, skuDTO);
            // 是否超出最大购买数量
            this.isLimitMaxBuy(skuDTO, sku.getLimitBuyCount());

            SkuOrderBO skuOrderBO = new SkuOrderBO(sku, skuDTO);
            // 累加总价
            finalTotalServerPrice = finalTotalServerPrice.add(skuOrderBO.getTotalPrice());
            skuOrderBOList.add(skuOrderBO);
        }

        if (orderDTO.getCouponIds() != null && !orderDTO.getCouponIds()
                                                        .isEmpty()) {
            // 当前用户是否拥有某张优惠券
            List<Coupon> userCouponList = this.couponService.getUserAllAvailableCouponIn(orderDTO.getCouponIds());
            if (userCouponList.isEmpty()) throw new NotFoundException(20011);
            if (orderDTO.getCouponIds()
                        .size() != userCouponList.size()) throw new NotFoundException(20011);

            CouponChecker couponChecker = new CouponChecker(userCouponList, skuOrderBOList);
            // 是否有多张相同品类的优惠券
            couponChecker.hasSameCategory();
            // 过期没
            couponChecker.isExpired();
            // 计算所有优惠券优惠过后的订单总价
            finalTotalServerPrice = couponChecker.calc(finalTotalServerPrice);
        }
    }


    /**
     * 是否下架或不存在
     *
     * @param size
     * @param size1
     */
    private void isSkuNotOnSale(Integer size, Integer size1) {
        if (!size.equals(size1)) throw new ParameterException(70003);
        if (size1 == 0) throw new NotFoundException(70003);
    }

    /**
     * 是否售罄或库存不足
     *
     * @param sku
     * @param skuDTO
     */
    private void isOutOfStock(Sku sku, SkuDTO skuDTO) {
        // 是否售罄
        if (sku.getStock() <= 0) throw new ParameterException(70002);
        // 购买数量是否超出库存
        if (skuDTO.getCount() > sku.getStock()) throw new ForbiddenException(70004);
    }

    /**
     * 是否超过最大购买限制
     *
     * @param skuDTO
     * @param maxBuyCount
     */
    private void isLimitMaxBuy(SkuDTO skuDTO, Long maxBuyCount) {
        if (maxBuyCount != null && maxBuyCount >= 0) {
            if (skuDTO.getCount() > maxBuyCount) throw new ParameterException(70005);
        } else {
            if (skuDTO.getCount() > this.limitBuyCount) throw new ParameterException(70005);
        }
    }

    /**
     * 计算某个规格商品的总价
     *
     * @param sku
     * @param skuDTO
     * @return
     */
    private BigDecimal calcSkuItemPrice(Sku sku, SkuDTO skuDTO) {
        if (skuDTO.getCount() <= 0) throw new ParameterException(70006);

        return sku.getActualPrice()
                  .multiply(new BigDecimal(skuDTO.getCount()));
    }
}
