package com.blcheung.missyou.core.enumeration;

import lombok.Getter;

@Getter
public enum LoginType {
    wx("微信登录"),    // 微信
    email("邮箱登录"); // 邮箱

    private String desc;

    LoginType(String desc) { this.desc = desc; }
}
