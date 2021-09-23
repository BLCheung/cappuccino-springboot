package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.Coupon;
import com.blcheung.missyou.service.CouponService;
import com.blcheung.missyou.vo.CouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
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
}
