package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoriesAllVO {
    private List<CategoryVO> root;
    private List<CategoryVO> subs;

    public CategoriesAllVO(List<Category> root, List<Category> subs) {
        this.root = root.stream()
                        .map(CategoryVO::new)
                        .collect(Collectors.toList());
        this.subs = subs.stream()
                        .map(CategoryVO::new)
                        .collect(Collectors.toList());
    }

}
