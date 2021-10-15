package com.blcheung.missyou.logic;

import com.blcheung.missyou.dto.SkuDTO;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.model.Sku;
import com.blcheung.missyou.model.SkuOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

@Service
// @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)    // 多例
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)   // 当前请求多例
public class OrderChecker {
    // 订单sku数据模型对象
    @Getter
    @Setter
    private List<SkuOrder> skuOrderList;

    @Value("${zbl.sku.limit_buy_count}")
    private Long limitBuyCount;

    public OrderChecker() {}

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
     * 获取订单的商品总数
     *
     * @return
     */
    public Long getOrderTotalCount() {
        return this.skuOrderList.stream()
                                .map(SkuOrder::getCount)
                                .reduce(Long::sum)
                                .orElse(0L);
    }

    /**
     * 获取订单所有商品总价
     *
     * @return
     */
    public BigDecimal getOrderTotalPrice() {
        return this.skuOrderList.stream()
                                .map(SkuOrder::getTotalPrice)
                                .reduce(BigDecimal::add)
                                .orElse(new BigDecimal("0"));
    }

    /**
     * 最终价是否ok
     *
     * @param finalTotalServerPrice
     */
    public void isFinalTotalServerPriceOK(BigDecimal finalTotalServerPrice) {
        // 天下没有免费的午餐!
        if (finalTotalServerPrice.compareTo(new BigDecimal("0")) <= 0) throw new ForbiddenException(70011);
    }


    /**
     * 是否下架或不存在
     *
     * @param serverSize
     * @param orderSize
     */
    public void isSkuNotOnSale(Integer serverSize, Integer orderSize) {
        if (!serverSize.equals(orderSize)) throw new ParameterException(70003);
        if (orderSize == 0) throw new NotFoundException(70003);
    }

    /**
     * 是否售罄或库存不足
     *
     * @param sku
     * @param skuDTO
     */
    public void isOutOfStock(Sku sku, SkuDTO skuDTO) {
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
    public void isLimitMaxBuy(SkuDTO skuDTO, Long maxBuyCount) {
        if (maxBuyCount != null && maxBuyCount >= 0) {
            if (skuDTO.getCount() > maxBuyCount) throw new ParameterException(70005);
        } else {
            if (skuDTO.getCount() > this.limitBuyCount) throw new ParameterException(70005);
        }
    }
}
