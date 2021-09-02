package com.blcheung.missyou.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
    private Integer code;
    private String  msg;
    private T       data;

    public Result(Integer code) {
        this(code, "", null);
    }

    public Result(Integer code, String msg) {
        this(code, msg, null);
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg  = msg;
        this.data = data;
    }
}
