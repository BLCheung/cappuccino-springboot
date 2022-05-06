package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.Banner;
import com.blcheung.cappuccino.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {
    @Autowired
    private BannerService bannerService;

    /**
     * 根据id获取Banner
     *
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")     // /id/1
    public Result<Banner> getById(@PathVariable @Positive Long id) {
        Banner banner = bannerService.getById(id);
        if (banner == null) throw new NotFoundException(30003);

        return ResultKit.resolve(banner);
    }

    /**
     * 根据name获取Banner
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")     // /name/a-1
    public Result<Banner> getByName(@PathVariable @NotBlank String name) {
        Banner banner = bannerService.getByName(name);
        if (banner == null) throw new NotFoundException(30003);

        return ResultKit.resolve(banner);
    }
}
