package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryItemVO {
    private Long    id;
    private String  name;
    private Boolean isRoot;
    private Long    parentId;
    private String  img;
    private Long    index;

    public CategoryItemVO(Category category) {
        BeanUtils.copyProperties(category, this);
    }
}
