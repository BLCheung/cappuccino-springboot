package com.blcheung.cappuccino.exception.http;

public class ParameterException extends HttpException {
    public ParameterException(int code) {
        this.code       = code;
        this.statusCode = 400;
    }
}
