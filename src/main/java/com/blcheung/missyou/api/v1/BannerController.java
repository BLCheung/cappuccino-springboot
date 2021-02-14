package com.blcheung.missyou.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/banner")
public class BannerController {

    //    @GetMapping("/test")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public String test() {
        return "Hello banner";
    }
}
