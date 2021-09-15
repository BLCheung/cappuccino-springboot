package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
public class PagingDTO {
    @DecimalMin(value = "1", message = "页码必须大于等于0")
    private Integer pageNum;
    @DecimalMin(value = "10", message = "每一页条目数至少为10")
    private Integer pageSize;
}
