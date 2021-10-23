package com.blcheung.missyou.exception.business;

public class Failed extends BusinessException {
    public Failed(String msg) {
        this(9999, msg);
    }

    public Failed(Integer code, String msg) {
        this(code, msg, 500);
    }

    public Failed(Integer code, String msg, Integer statusCode) {
        super(code, msg, statusCode);
    }
}
