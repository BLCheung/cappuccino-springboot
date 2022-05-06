package com.blcheung.cappuccino.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 下单时前端传递的商品DTO
 */
@Getter
@Setter
public class SkuDTO {
    @NotNull(message = "商品规格不能为空")
    private Long id;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量不合法")
    @Min(value = 1, message = "购买数量必须大于等于1")
    private Long count;
}
