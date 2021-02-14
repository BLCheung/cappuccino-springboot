package com.blcheung.missyou.exception.http;

public class ForbiddenException extends HttpException {

    public ForbiddenException(int code) {
        this.code = code;
        this.statusCode = 403;
    }
}
