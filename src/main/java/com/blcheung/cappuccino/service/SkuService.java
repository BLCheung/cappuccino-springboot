package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.model.Sku;
import com.blcheung.cappuccino.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    public Sku getSkuById(Long skuId) {
        Optional<Sku> skuOptional = Optional.ofNullable(this.skuRepository.findFirstById(skuId));
        return skuOptional.orElseThrow(() -> new NotFoundException(60001));
    }

    public List<Sku> getAllSkuByIds(List<Long> ids) {
        List<Sku> skuList = this.skuRepository.findAllByIdIn(ids);
        if (skuList.isEmpty()) return Collections.emptyList();

        return skuList;
    }
}
