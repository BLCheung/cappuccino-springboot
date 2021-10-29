package com.blcheung.missyou.kit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 金额计算工具类
 */
// @ConfigurationProperties(prefix = "zbl.money")
@Component
public class MoneyKit {
    // 价格运算取整模式
    private static int mode;

    @Value("${zbl.money.mode}")
    public void setMode(int mode) {
        MoneyKit.mode = mode;
    }

    /**
     * 计算满减价
     *
     * @param originPrice 原价
     * @param minusPrice  满减整数价
     * @return
     */
    public static BigDecimal discountMinusPrice(BigDecimal originPrice, BigDecimal minusPrice) {
        return originPrice.subtract(minusPrice);
    }

    /**
     * 计算满减折扣价
     *
     * @param originPrice 原价
     * @param offRate     折扣率（小数）
     * @return
     */
    public static BigDecimal discountOffPrice(BigDecimal originPrice, BigDecimal offRate) {
        return originPrice.multiply(offRate)
                          .setScale(2, MoneyKit.mode);
    }

    /**
     * 把BigDecimal价格转换成分为单位
     *
     * @param price
     * @return
     */
    public static BigDecimal transferToFen(BigDecimal price) {
        return price.multiply(new BigDecimal("100"));
    }
}
