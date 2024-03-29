package com.blcheung.cappuccino.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 下单时传递的订单DTO
 */
@Getter
@Setter
public class CreateOrderDTO {
    @NotNull(message = "购买的商品集合不能为空")
    @Valid
    private List<SkuDTO> skuList;

    @NotNull(message = "收货地址不能为空")
    private Long       addressId;

    private List<Long> couponIds;

    private String     remark;
}
