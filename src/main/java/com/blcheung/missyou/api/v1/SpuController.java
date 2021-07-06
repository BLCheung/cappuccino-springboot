package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.service.SpuService;
import com.blcheung.missyou.vo.SpuLatestPagingVO;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {
    @Autowired
    private SpuService spuService;

    /**
     * 获取Sou详情
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
    public List<SpuLatestPagingVO> getLatestPagingSpu() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<Spu> spuList = this.spuService.getLatestPagingSpu();
        List<SpuLatestPagingVO> vos = new ArrayList<>();

        spuList.forEach(spu -> vos.add(mapper.map(spu, SpuLatestPagingVO.class)));
        return vos;
    }
}
