package com.blcheung.cappuccino.exception.http;

public class UnAuthorizedException extends HttpException {
    public UnAuthorizedException(int code) {
        this.code       = code;
        this.statusCode = 401;
    }
}
