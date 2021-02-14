package com.blcheung.missyou.exception.http;

public class HttpException extends RuntimeException {
    protected Integer code;
    protected Integer statusCode = 500;

    public Integer getCode() {
        return code;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
