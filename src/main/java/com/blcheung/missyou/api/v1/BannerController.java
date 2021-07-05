package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.Banner;
import com.blcheung.missyou.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

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
    @GetMapping("/id/{id}")
    public Banner getById(@PathVariable Long id) {
        return bannerService.getById(id);
    }

    /**
     * 根据name获取Banner
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")
    public Banner getByName(@PathVariable @NotBlank String name) {
        Banner banner = bannerService.getByName(name);
        if (banner == null) throw new NotFoundException(30005);

        return banner;
    }
}
