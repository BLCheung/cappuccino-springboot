package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpuRepository extends JpaRepository<Spu, Long> {

    Spu findOneById(Long id);


    /**
     * 根据分类id查询出spu
     *
     * @param categoryId
     * @param pageable
     * @return
     */
    Page<Spu> findByCategoryId(Long categoryId, Pageable pageable);
    // "select * from spu where categoryId = categoryId"

    /**
     * 查询出对应父级分类的spu
     *
     * @param rootCategoryId
     * @param pageable
     * @return
     */
    Page<Spu> findByRootCategoryId(Long rootCategoryId, Pageable pageable);
}
