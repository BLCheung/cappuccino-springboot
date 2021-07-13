package com.blcheung.missyou.exception.http;

public class ServerErrorException extends HttpException {
    public ServerErrorException(int code) {
        this.code       = code;
        this.statusCode = 500;
    }
}
