package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
    Sku findFirstById(Long skuId);

    List<Sku> findAllByIdIn(List<Long> ids);
}
