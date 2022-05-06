package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.Activity;
import com.blcheung.cappuccino.service.ActivityService;
import com.blcheung.cappuccino.vo.ActivityCouponVO;
import com.blcheung.cappuccino.vo.ActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    /**
     * 获取活动
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")
    public Result<ActivityVO> getActivityByName(@PathVariable @NotBlank(message = "必须传入活动名称") String name) {
        Optional<Activity> activityOptional = Optional.ofNullable(activityService.getActivityByName(name));
        return ResultKit.resolve(new ActivityVO(activityOptional.orElseThrow(() -> new NotFoundException(40001))));
    }

    /**
     * 获取活动
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}/with_coupon")
    public Result<ActivityCouponVO> getActivityByNameWithCoupon(
            @PathVariable @NotBlank(message = "必须传入活动名称") String name) {
        Optional<Activity> activityOptional = Optional.ofNullable(activityService.getActivityByName(name));
        return ResultKit.resolve(
                new ActivityCouponVO(activityOptional.orElseThrow(() -> new NotFoundException(40001))));
    }
}
