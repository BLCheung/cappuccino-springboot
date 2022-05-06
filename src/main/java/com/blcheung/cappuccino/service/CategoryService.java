package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.model.Category;
import com.blcheung.cappuccino.model.GridCategory;
import com.blcheung.cappuccino.repository.CategoryRepository;
import com.blcheung.cappuccino.repository.GridCategoryRepository;
import com.blcheung.cappuccino.vo.CategoriesAllVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository     categoryRepository;
    @Autowired
    private GridCategoryRepository gridCategoryRepository;

    public CategoriesAllVO getAllCategories() {
        List<Category> roots = this.categoryRepository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subs = this.categoryRepository.findAllByIsRootOrderByIndexAsc(false);

        //        Map<String, List<CategoryVO>> listMap = new HashMap<>();
        //        listMap.put("root", roots);
        //        listMap.put("sub", subs);
        return new CategoriesAllVO(roots, subs);
    }

    public List<GridCategory> getGridCategories() {
        List<GridCategory> categories = this.gridCategoryRepository.findAll();
        if (categories.isEmpty()) throw new NotFoundException(30004);
        return categories;
    }

    public Category getCategoryWithCoupon(Long id) {
        Optional<Category> categoryWithCoupon = Optional.ofNullable(this.categoryRepository.findCategoryById(id));
        return categoryWithCoupon.orElseThrow(() -> new NotFoundException(40001));
    }
}
