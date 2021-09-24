package com.blcheung.missyou.exception;

import com.blcheung.missyou.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code) {
        this.code       = code;
        this.statusCode = 201;
    }
}
