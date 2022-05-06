package com.blcheung.cappuccino.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;

@Getter
@Setter
public class PagingDTO {
    @DecimalMin(value = "1", message = "页码必须大于1")
    private Integer pageNum;
    @DecimalMin(value = "1", message = "每一页条目数至少为1")
    private Integer pageSize;
}
