package com.blcheung.missyou.exception.business;

public class Success extends BusinessException {
    public Success(String msg) {
        this(0, msg, 200);
    }

    public Success(String msg, Integer statusCode) {
        this(0, msg, statusCode);
    }

    public Success(Integer code, String msg, Integer statusCode) {
        super(code, msg, statusCode);
    }
}
