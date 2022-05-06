package com.blcheung.cappuccino.core;

public class GlobalResponseData {
    private int code;
    private String msg;
    private String request;

    public GlobalResponseData(int code, String msg, String request) {
        this.code = code;
        this.msg = msg;
        this.request = request;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getRequest() {
        return request;
    }
}
