package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.service.ThemeService;
import com.blcheung.missyou.vo.ThemeVO;
import com.blcheung.missyou.vo.ThemeSpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<List<ThemeVO>> getThemesByNames(@RequestParam(name = "names") String names) {
        List<ThemeVO> themes = this.themeService.getThemeByNames(names);
        if (themes.isEmpty()) throw new NotFoundException(30003);

        return ResultKit.resolve(themes);
    }

    /**
     * 获取一个专题（含Spu）
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}/with_spu")
    public Result<ThemeSpuVO> getThemeByNameWithSpu(@PathVariable(name = "name") String name) {
        Optional<ThemeSpuVO> themeWithSpu = Optional.ofNullable(this.themeService.getThemeWithSpu(name));

        return ResultKit.resolve(themeWithSpu.orElseThrow(() -> new NotFoundException(30003)));
    }
}
