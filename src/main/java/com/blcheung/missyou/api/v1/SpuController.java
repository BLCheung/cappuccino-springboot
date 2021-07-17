package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.dto.PagingDTO;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.service.SpuService;
import com.blcheung.missyou.vo.PagingResultDozer;
import com.blcheung.missyou.vo.SpuPagingVO;
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
    public Spu getSpu(@RequestParam @Positive Long id) {
        Spu spu = this.spuService.getSpu(id);
        if (spu == null) {
            throw new NotFoundException(30002);
        }

        return spu;
    }

    /**
     * 获取最新的商品分页
     *
     * @return
     */
    @PostMapping("/latest")  // latest  {pageNum:0,pageSize:10}
    public PagingResultDozer<Spu, SpuPagingVO> getLatestPagingSpu(@RequestBody @Validated PagingDTO pagingDTO) {
        Page<Spu> spuList = this.spuService.getLatestPagingSpu(pagingDTO.getPageNum(), pagingDTO.getPageSize());

        return new PagingResultDozer<>(spuList, SpuPagingVO.class);
    }


    /**
     * 根据获取分类Spu
     *
     * @param categoryId
     * @param isRoot
     * @return
     */
    @PostMapping("/by/category/{categoryId}")
    public PagingResultDozer<Spu, SpuPagingVO> getByCategoryId(
            @PathVariable(name = "categoryId") @Positive() Long categoryId, @RequestParam(name = "isRoot",
                                                                                          defaultValue = "false") Boolean isRoot,
            @RequestBody @Validated PagingDTO pagingDTO) {

        Page<Spu> spuPage = this.spuService.getByCategory(categoryId, isRoot, pagingDTO.getPageNum(),
                                                          pagingDTO.getPageSize());

        return new PagingResultDozer<>(spuPage, SpuPagingVO.class);
    }
}
