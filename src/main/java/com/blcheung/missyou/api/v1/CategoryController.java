package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.model.GridCategory;
import com.blcheung.missyou.service.CategoryService;
import com.blcheung.missyou.vo.CategoriesAllVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类（1级和2级）
     *
     * @return
     */
    @GetMapping("/all")
    public CategoriesAllVO getAll() {
        return this.categoryService.getAllCategories();
    }

    /**
     * 获取六宫格的分类
     *
     * @return
     */
    @GetMapping("/grid")
    public List<GridCategory> getGird() {
        return this.categoryService.getGridCategories();
    }
}
