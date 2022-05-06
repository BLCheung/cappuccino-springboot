package com.blcheung.cappuccino.exception.business;

import com.blcheung.cappuccino.exception.http.HttpException;

public class BusinessException extends HttpException {

    public BusinessException(Integer code, String msg, Integer statusCode) {
        this.code       = code;
        this.msg        = msg;
        this.statusCode = statusCode;
    }
}
