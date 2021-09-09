package com.blcheung.missyou.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenDTO {
    @NotBlank(message = "token不能为空")
    private String token;
}
