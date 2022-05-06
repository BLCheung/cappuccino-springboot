package com.blcheung.cappuccino.exception.http;

public class HttpException extends RuntimeException {
    protected Integer code;
    protected Integer statusCode = 500;
    protected String  msg;

    public Integer getCode() {
        return code;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
