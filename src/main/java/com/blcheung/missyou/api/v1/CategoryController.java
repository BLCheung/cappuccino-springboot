package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.kit.ResultKit;
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
    public Result<CategoriesAllVO> getAll() { return ResultKit.resolve(this.categoryService.getAllCategories()); }

    /**
     * 六宫格的分类
     *
     * @return
     */
    @GetMapping("/grid")
    public Result<List<GridCategory>> getGird() {
        List<GridCategory> categories = this.categoryService.getGridCategories();
        if (categories.isEmpty()) throw new NotFoundException(30009);

        return ResultKit.resolve(categories);
    }
}
