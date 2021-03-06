package com.blcheung.missyou.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @GetMapping("/name/{name}")
    public void getByName(@PathVariable @NotBlank String name) {

    }
}
