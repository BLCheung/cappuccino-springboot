package com.blcheung.missyou.service;

import com.blcheung.missyou.model.Spu;
import com.blcheung.missyou.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuService {
    @Autowired
    SpuRepository spuRepository;

    /**
     * 获取Spu详情
     * @param id
     * @return
     */
    public Spu getSpu(Long id) {
        return this.spuRepository.findOneById(id);
    }

    /**
     * 获取最新的spu分页
     * @return
     */
    public List<Spu> getLatestPagingSpu() {
        return this.spuRepository.findAll();
    }
}
