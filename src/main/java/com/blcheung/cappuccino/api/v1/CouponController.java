package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.core.annotations.ScopeLevel;
import com.blcheung.cappuccino.core.enumeration.CouponStatus;
import com.blcheung.cappuccino.exception.http.ParameterException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.Coupon;
import com.blcheung.cappuccino.service.CouponService;
import com.blcheung.cappuccino.vo.CouponCategoryVO;
import com.blcheung.cappuccino.vo.CouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 通过分类id获取优惠券
     *
     * @param cid
     * @return
     */
    @GetMapping("/by/category/{cid}")
    public Result<List<CouponVO>> getCouponByCategoryId(@PathVariable @NotBlank Long cid) {
        List<Coupon> couponList = this.couponService.getCouponByCategoryId(cid);

        return ResultKit.resolve(CouponVO.buildCouponList(couponList));
    }

    /**
     * 获取全场券
     *
     * @return
     */
    @GetMapping("/whole_store")
    public Result<List<CouponVO>> getCouponByIsWholeStore() {
        List<Coupon> couponList = this.couponService.getCouponByIsWholeStore(true);
        return ResultKit.resolve(CouponVO.buildCouponList(couponList));
    }

    /**
     * 领取优惠券
     *
     * @param couponId
     */
    @GetMapping("/receive/{couponId}")
    @ScopeLevel()
    public void receiveCoupon(@PathVariable @NotBlank Long couponId) {
        this.couponService.receiveCoupon(couponId);
        ResultKit.createSuccess();
    }

    /**
     * 获取我的优惠券
     *
     * @param status 优惠券状态类型
     * @return
     */
    @GetMapping("/my/by/status/{status}")
    @ScopeLevel()
    public Result<List<CouponVO>> getCouponByStatus(@PathVariable @NotNull(message = "请传入优惠券状态") Integer status) {
        CouponStatus couponStatus = CouponStatus.toType(status)
                                                .orElseThrow(() -> new ParameterException(50000));

        List<Coupon> couponList;
        switch (couponStatus) {
            case AVAILABLE:
                couponList = this.couponService.getMyAvailableCoupon();
                break;
            case USED:
                couponList = this.couponService.getMyUsedCoupon();
                break;
            case EXPIRED:
                couponList = this.couponService.getMyExpiredCoupon();
                break;

            default:
                throw new ParameterException(5000);
        }

        return ResultKit.resolve(CouponVO.buildCouponList(couponList));
    }

    /**
     * 获取我可用的优惠券（带分类）
     * 可用于下单时校验该订单适用的优惠券
     *
     * @return
     */
    @GetMapping("/my/available/with_category")
    @ScopeLevel()
    public Result<List<CouponCategoryVO>> getMyAvailableCouponWithCategory() {
        List<Coupon> couponList = this.couponService.getMyAvailableCoupon();
        if (couponList.isEmpty()) return ResultKit.resolve(Collections.emptyList());

        return ResultKit.resolve(CouponCategoryVO.buildCouponCategoryList(couponList));
    }
}
