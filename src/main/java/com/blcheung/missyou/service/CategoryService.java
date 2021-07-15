package com.blcheung.missyou.service;

import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.Category;
import com.blcheung.missyou.model.GridCategory;
import com.blcheung.missyou.repository.CategoryRepository;
import com.blcheung.missyou.repository.GridCategoryRepository;
import com.blcheung.missyou.vo.CategoriesAllVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository     categoryRepository;
    @Autowired
    private GridCategoryRepository gridCategoryRepository;

    public CategoriesAllVO getAllCategories() {
        List<Category> roots = this.categoryRepository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subs = this.categoryRepository.findAllByIsRootOrderByIndexAsc(false);

        //        Map<String, List<CategoryItemVO>> listMap = new HashMap<>();
        //        listMap.put("root", roots);
        //        listMap.put("sub", subs);
        return new CategoriesAllVO(roots, subs);
    }

    public List<GridCategory> getGridCategories() {
        List<GridCategory> categories = this.gridCategoryRepository.findAll();
        if (categories.isEmpty()) throw new NotFoundException(30009);
        return categories;
    }
}
