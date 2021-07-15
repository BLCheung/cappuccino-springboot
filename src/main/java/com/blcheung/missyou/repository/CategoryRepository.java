package com.blcheung.missyou.repository;

import com.blcheung.missyou.model.Category;
import com.blcheung.missyou.vo.CategoryItemVO;
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


}
