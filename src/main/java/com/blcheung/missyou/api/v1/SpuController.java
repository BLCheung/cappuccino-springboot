package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/id/{id}/detail")
    public Spu getSpu(@PathVariable @Positive Long id) {
        Spu spu = this.spuService.getSpu(id);
        if (spu == null) {
            throw new NotFoundException(30001);
        }

        return spu;
    }

    /**
     * 获取最新的商品分页
     *
     * @return
     */
    @GetMapping("/latest")
    public List<Spu> getLatestPagingSpu() {
        return this.spuService.getLatestPagingSpu();
    }
}
