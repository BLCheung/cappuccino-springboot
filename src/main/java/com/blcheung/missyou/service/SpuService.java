package com.blcheung.missyou.service;

import com.blcheung.missyou.dto.CategorySpuPagingDTO;
import com.blcheung.missyou.dto.PagingDTO;
import com.blcheung.missyou.kit.PagingKit;
import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpuService {
    @Autowired
    private SpuRepository spuRepository;

    /**
     * 获取Spu详情
     *
     * @param id
     * @return
     */
    public Spu getSpu(Long id) {
        return this.spuRepository.findOneById(id);
    }

    /**
     * 获取最新的spu分页
     *
     * @param pagingDTO
     * @return
     */
    public Page<Spu> getLatestPagingSpu(PagingDTO pagingDTO) {
        Pageable pageable = PagingKit.buildLatest(pagingDTO);
        return this.spuRepository.findAll(pageable);
    }

    /**
     * 查询分类spu
     *
     * @param categorySpuPagingDTO
     * @return
     */
    public Page<Spu> getByCategory(CategorySpuPagingDTO categorySpuPagingDTO) {
        Pageable pageable = PagingKit.buildLatest(categorySpuPagingDTO);

        if (categorySpuPagingDTO.getIsRoot()) {
            return this.spuRepository.findByRootCategoryId(categorySpuPagingDTO.getCategoryId(), pageable);
        }

        return this.spuRepository.findByCategoryId(categorySpuPagingDTO.getCategoryId(), pageable);
    }
}
