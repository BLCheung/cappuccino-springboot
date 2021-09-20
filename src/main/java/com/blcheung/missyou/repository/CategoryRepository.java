package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * 根据正序找到所有一级分类
     *
     * @param isRoot 是否一级分类
     * @return
     */
    List<Category> findAllByIsRootOrderByIndexAsc(Boolean isRoot);

    /**
     * 通过id找分类
     * @param id
     * @return
     */
    Category findCategoryById(Long id);
}
