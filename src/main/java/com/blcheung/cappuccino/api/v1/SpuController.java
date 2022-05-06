package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.dto.CategorySpuPagingDTO;
import com.blcheung.cappuccino.dto.PagingDTO;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.Spu;
import com.blcheung.cappuccino.service.SpuService;
import com.blcheung.cappuccino.vo.PagingResultDozer;
import com.blcheung.cappuccino.vo.SpuPagingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

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
    @GetMapping("/detail")   // /detail?id=
    public Result<Spu> getSpu(@RequestParam @Positive Long id) {
        Spu spu = this.spuService.getSpu(id);
        if (spu == null) {
            throw new NotFoundException(60001);
        }

        return ResultKit.resolve(spu);
    }

    /**
     * 获取最新的商品分页
     *
     * @param pagingDTO
     * @return
     */
    @PostMapping("/latest")  // latest  {pageNum:0,pageSize:10}
    public Result<PagingResultDozer<Spu, SpuPagingVO>> getLatestPagingSpu(@RequestBody @Validated PagingDTO pagingDTO) {
        Page<Spu> spuList = this.spuService.getLatestPagingSpu(pagingDTO);

        return ResultKit.resolve(new PagingResultDozer<>(spuList, SpuPagingVO.class));
    }


    /**
     * 根据获取分类Spu
     *
     * @param categorySpuPagingDTO
     * @return
     */
    @PostMapping("/by/category")
    public Result<PagingResultDozer<Spu, SpuPagingVO>> getByCategoryId(
            @RequestBody @Validated CategorySpuPagingDTO categorySpuPagingDTO) {

        Page<Spu> spuPage = this.spuService.getByCategory(categorySpuPagingDTO);

        return ResultKit.resolve(new PagingResultDozer<>(spuPage, SpuPagingVO.class));
    }
}
