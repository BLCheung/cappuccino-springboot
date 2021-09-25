package com.blcheung.missyou.core.enumeration;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum LoginType {
    PHONE(0, "手机登录"),  // 手机
    WX(1, "微信登录");    // 微信

    private Integer value;


    LoginType(Integer value, String desc) { this.value = value; }

    public static Optional<LoginType> toType(Integer value) {
        return Stream.of(LoginType.values())
                     .filter(loginType -> value.equals(loginType.getValue()))
                     .findAny();
    }
}
