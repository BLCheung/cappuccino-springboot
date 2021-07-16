package com.blcheung.missyou.service;

import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
     * @return
     */
    public Page<Spu> getLatestPagingSpu(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by("createTime")  // 因为jpa操作的也是model，所以为model字段
                                                                        .descending());
        return this.spuRepository.findAll(pageRequest);
    }

    /**
     * 查询分类spu
     *
     * @param categoryId 分类id
     * @param isRoot     是否为父节点分类
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<Spu> getByCategory(Long categoryId, Boolean isRoot, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        if (isRoot) {
            return this.spuRepository.findByRootCategoryIdOrderByCreateTime(categoryId, pageRequest);
        }

        return this.spuRepository.findByCategoryIdOrderByCreateTimeDesc(categoryId, pageRequest);
    }
}
