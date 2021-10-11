package com.blcheung.missyou.logic;

import com.blcheung.missyou.bo.SkuOrderBO;
import com.blcheung.missyou.core.enumeration.CouponType;
import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.kit.MoneyKit;
import com.blcheung.missyou.model.Category;
import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.util.CommonUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// @Service
public class CouponChecker {
    private final List<Coupon>     couponList;
    private final List<SkuOrderBO> skuOrderBOList;

    public CouponChecker(List<Coupon> couponList, List<SkuOrderBO> skuOrderBOList) {
        this.couponList     = couponList;
        this.skuOrderBOList = skuOrderBOList;
    }

    /**
     * 是否同时存在相同品类的优惠券
     */
    public void hasSameCategory() {
        List<Long> categoryIds = this.couponList.stream()
                                                .map(Coupon::getCategoryList)
                                                .flatMap(Collection::stream)
                                                .map(Category::getId)
                                                .collect(Collectors.toList());
        long uniqueCategorySize = categoryIds.stream()
                                             .distinct()
                                             .count();
        // 订单同时存在多张相同品类的优惠券
        if (categoryIds.size() != uniqueCategorySize) throw new ForbiddenException(70010);
    }

    /**
     * 优惠券是否过期
     */
    public void isExpired() {
        // 是否有优惠券过期
        Date now = new Date();
        couponList.stream()
                  .filter(coupon -> !CommonUtils.isInRangeDate(now, coupon.getStartTime(), coupon.getEndTime()))
                  .findFirst()
                  .ifPresent((coupon) -> { throw new ForbiddenException(50002); });
    }

    /**
     * 计算所有优惠券优惠价
     *
     * @param totalServerPrice 订单总价
     * @return
     */
    public BigDecimal calc(BigDecimal totalServerPrice) {
        // 优惠券累计优惠总价
        BigDecimal totalCouponMinusPrice = this.couponList.stream()
                                                          .map(coupon -> this.calcCouponPrice(coupon, totalServerPrice))
                                                          .reduce(BigDecimal::add)
                                                          .orElse(new BigDecimal("0"));

        // 天下没有免费的午餐!
        if (totalCouponMinusPrice.compareTo(totalServerPrice) >= 0) throw new ForbiddenException(70011);

        return MoneyKit.discountMinusPrice(totalServerPrice, totalCouponMinusPrice);
    }

    /**
     * 计算优惠券价格
     *
     * @param coupon           优惠券
     * @param totalServerPrice 订单总价
     * @return
     */
    private BigDecimal calcCouponPrice(Coupon coupon, BigDecimal totalServerPrice) {
        CouponType couponType = CouponType.toType(coupon.getType())
                                          .orElseThrow(() -> new ForbiddenException(50004));
        // 当前优惠券总计优惠总价
        BigDecimal couponMinusPrice = null;
        // 需要计算优惠条件的总价
        BigDecimal conditionTotalPrice = null;

        // 是否为全场券，无视品类
        if (coupon.getWholeStore()) {
            // 所有商品总价为判断条件
            conditionTotalPrice = totalServerPrice;
        } else {
            // 有品类限制，该优惠券品类范围内的所有商品总价为判断条件
            conditionTotalPrice = this.getSumPriceByCouponCategory(coupon);
        }

        // 所计算的价格是否符合优惠券的满减
        this.isFullMoneyOK(coupon, conditionTotalPrice);

        switch (couponType) {
            case NO_THRESHOLD:
            case FULL_MINUS:
                couponMinusPrice = coupon.getMinus();
                break;
            case FULL_OFF:
                couponMinusPrice = MoneyKit.discountOffPrice(conditionTotalPrice,
                                                             MoneyKit.discountMinusPrice(new BigDecimal("1"),
                                                                                         coupon.getRate()));
                break;
        }

        return couponMinusPrice;
    }


    /**
     * 获取优惠券对应品类下的sku总价
     *
     * @param coupon
     * @return
     */
    private BigDecimal getSumPriceByCouponCategory(Coupon coupon) {
        List<Category> categories = coupon.getCategoryList();
        return categories.stream()
                         .map(category -> this.getSumPriceByCategory(this.skuOrderBOList, category.getId()))
                         .reduce(BigDecimal::add)
                         .orElse(new BigDecimal("0"));
    }

    /**
     * 获取某个品类下所有匹配的sku总价
     *
     * @param skuOrderBOList
     * @param categoryId
     * @return
     */
    private BigDecimal getSumPriceByCategory(List<SkuOrderBO> skuOrderBOList, Long categoryId) {
        return skuOrderBOList.stream()
                             .filter(skuOrderBO -> skuOrderBO.getCategoryId()
                                                             .equals(categoryId))
                             .map(SkuOrderBO::getTotalPrice)
                             .reduce(BigDecimal::add)
                             .orElse(new BigDecimal("0"));
    }

    /**
     * 价格是否符合优惠券满减条件
     *
     * @param coupon        优惠券
     * @param categoryPrice 适用品类的价格总和
     * @return
     */
    private void isFullMoneyOK(Coupon coupon, BigDecimal categoryPrice) {
        this.validateCoupon(coupon);
        BigDecimal fullMoney = coupon.getFullMoney();

        switch (CouponType.toType(coupon.getType())
                          .get()) {
            case FULL_MINUS:
            case FULL_OFF:
                if (categoryPrice.compareTo(fullMoney) < 0) throw new ForbiddenException(70008);
                break;
            case NO_THRESHOLD:
                break;
            default:
                throw new ForbiddenException(50004);
        }
    }

    /**
     * 校验优惠券价格是否合法
     *
     * @param coupon
     */
    private void validateCoupon(Coupon coupon) {
        CouponType couponType = CouponType.toType(coupon.getType())
                                          .orElseThrow(() -> new ForbiddenException(50004));
        BigDecimal zero = new BigDecimal("0");
        BigDecimal minus = coupon.getMinus();
        BigDecimal fullMoney = coupon.getFullMoney();

        switch (couponType) {
            case NO_THRESHOLD:
                if (minus == null || minus.compareTo(zero) <= 0) throw new ParameterException(50000);
                break;
            case FULL_MINUS:
                if (minus == null || minus.compareTo(zero) <= 0) throw new ParameterException(50000);
                if (fullMoney == null || minus.compareTo(zero) <= 0) throw new ParameterException(50000);
                break;
            case FULL_OFF:
                BigDecimal rate = coupon.getRate();
                if (rate == null || minus.compareTo(zero) <= 0) throw new ParameterException(50000);
                if (fullMoney == null || minus.compareTo(zero) <= 0) throw new ParameterException(50000);
                break;
        }
    }
}
