package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategorySpuPagingDTO extends PagingDTO {
    /**
     * 分类id
     */
    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    /**
     * 是否一级分类
     */
    private Boolean isRoot;
}
