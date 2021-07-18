package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.service.ThemeService;
import com.blcheung.missyou.vo.ThemeItemVO;
import com.blcheung.missyou.vo.ThemeSpuItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    /**
     * 一组专题
     *
     * @param names
     * @return
     */
    @GetMapping("/by/names")
    public List<ThemeItemVO> getThemesByNames(@RequestParam(name = "names") String names) {
        List<ThemeItemVO> themes = this.themeService.getThemeByNames(names);
        if (themes.isEmpty()) throw new NotFoundException(30003);

        return themes;
    }

    /**
     * 获取一个专题（含Spu）
     *
     * @param name
     * @return
     */
    @GetMapping("/by/name/with_spu")
    public ThemeSpuItemVO getThemeByNameWithSpu(@RequestParam(name = "name") String name) {
        Optional<ThemeSpuItemVO> themeWithSpu = Optional.ofNullable(this.themeService.getThemeWithSpu(name));

        return themeWithSpu.orElseThrow(() -> new NotFoundException(30003));
    }
}
