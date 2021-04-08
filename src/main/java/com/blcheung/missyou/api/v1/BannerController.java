package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
@Validated
public class BannerController {
    @Autowired
    private BannerService bannerService;

//    @GetMapping("/name/{name}")
//    public Banner getByName(@PathVariable @NotBlank String name) {
//        Banner banner = bannerService.getByName(name);
//        return banner;
//    }
}
