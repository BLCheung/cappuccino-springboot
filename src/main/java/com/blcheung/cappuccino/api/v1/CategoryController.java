package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.model.GridCategory;
import com.blcheung.cappuccino.service.CategoryService;
import com.blcheung.cappuccino.vo.CategoriesAllVO;
import com.blcheung.cappuccino.vo.CategoryCouponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        if (categories.isEmpty()) throw new NotFoundException(30004);

        return ResultKit.resolve(categories);
    }

    /**
     * 获取包含优惠券的分类
     *
     * @param id
     * @return
     */
    @GetMapping("{id}/with_coupon")
    public Result<CategoryCouponVO> getCategoryWithCoupon(@PathVariable Long id) {
        return ResultKit.resolve(new CategoryCouponVO(this.categoryService.getCategoryWithCoupon(id)));
    }
}
