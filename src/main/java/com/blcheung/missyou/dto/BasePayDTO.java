package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasePayDTO {
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
