package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.model.Activity;
import com.blcheung.missyou.service.ActivityService;
import com.blcheung.missyou.vo.ActivityCouponVO;
import com.blcheung.missyou.vo.ActivityVO;
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
