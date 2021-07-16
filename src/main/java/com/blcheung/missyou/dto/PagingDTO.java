package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
public class PagingDTO {
    @Min(value = 0, message = "页码必须大于等于0")
    private Integer pageNum;
    @Min(value = 1, message = "每一页条目数至少为1")
    private Integer pageSize;
}
