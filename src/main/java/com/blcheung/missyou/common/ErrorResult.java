package com.blcheung.missyou.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResult extends Result {
    private String request;

    public ErrorResult(Integer code, String msg, String request) {
        super(code, msg, null);
        this.request = request;
    }
}
