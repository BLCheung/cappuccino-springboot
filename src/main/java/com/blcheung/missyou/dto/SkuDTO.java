package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 下单时前端传递的商品DTO
 */
@Getter
@Setter
public class SkuDTO {
    private Long    id;
    private Integer count;
}
