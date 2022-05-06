package com.blcheung.cappuccino.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
public class OrderPagingDTO extends PagingDTO {
    @NotNull(message = "订单状态不能给空")
    private Integer status;
}
