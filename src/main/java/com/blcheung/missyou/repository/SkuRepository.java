package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
    Sku findFirstById(Long skuId);

    List<Sku> findAllByIdIn(List<Long> ids);

    @Modifying
    @Query("update Sku s\n" + "set s.stock = s.stock - :count\n" + "where s.id = :sid\n" + "and s.stock >= :count")
    int reduceStock(Long sid, Long count);

    @Modifying
    @Query("update Sku s set s.stock = s.stock + :count\n" + "where s.id = :sid")
    int recoverStock(Long sid, Long count);
}
