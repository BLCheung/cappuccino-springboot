package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryVO {
    private Long    id;
    private String  name;
    private Boolean isRoot;
    private Long    parentId;
    private String  img;
    private Long    index;

    public CategoryVO(Category category) {
        BeanUtils.copyProperties(category, this);
    }
}
