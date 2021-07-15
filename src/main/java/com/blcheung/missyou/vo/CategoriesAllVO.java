package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoriesAllVO {
    private List<CategoryItemVO> root;
    private List<CategoryItemVO> subs;

    public CategoriesAllVO(List<Category> root, List<Category> subs) {
        this.root = root.stream()
                        .map(CategoryItemVO::new)
                        .collect(Collectors.toList());
        this.subs = subs.stream()
                        .map(CategoryItemVO::new)
                        .collect(Collectors.toList());
    }

}
