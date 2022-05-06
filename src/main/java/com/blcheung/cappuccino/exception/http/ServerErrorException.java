package com.blcheung.cappuccino.exception.http;

public class ServerErrorException extends HttpException {
    public ServerErrorException(int code) {
        this.code       = code;
        this.statusCode = 500;
    }
}
