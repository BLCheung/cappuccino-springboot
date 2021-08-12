package com.blcheung.missyou.dto;

import com.blcheung.missyou.core.enumeration.LoginType;
import com.blcheung.missyou.dto.validator.EnumField;
import com.blcheung.missyou.dto.validator.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不能为空")
    private String account;
    @TokenPassword()
    private String password;
    @EnumField(value = LoginType.class,
               canNull = false,
               message = "登录类型不正确")
    private String type;
}
