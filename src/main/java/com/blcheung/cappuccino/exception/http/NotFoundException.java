package com.blcheung.cappuccino.exception.http;

public class NotFoundException extends HttpException {
    public NotFoundException(int code) {
        this.code = code;
        this.statusCode = 404;
    }
}
