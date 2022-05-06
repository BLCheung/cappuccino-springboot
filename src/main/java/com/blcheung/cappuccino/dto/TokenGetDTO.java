package com.blcheung.cappuccino.dto;

import com.blcheung.cappuccino.dto.validator.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不能为空")
    private String account;

    @TokenPassword()
    private String password;

    @NotNull(message = "登录类型不能为空")
    private Integer type;
}
