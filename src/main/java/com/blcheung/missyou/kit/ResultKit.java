package com.blcheung.missyou.kit;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.configuration.ExceptionCodeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 提供给业务的结果封装类
 */
@Component
public class ResultKit {
    private static ExceptionCodeConfiguration codeConfiguration;

    @Autowired
    public void setCodeConfiguration(ExceptionCodeConfiguration codeConfiguration) {
        ResultKit.codeConfiguration = codeConfiguration;
    }

    public static <T> Result<T> resolve(T data) {
        return new Result<>(0, codeConfiguration.getMessage(0), data);
    }

    public static Result reject() {
        return new Result(9999, codeConfiguration.getMessage(9999), null);
    }

    public static Result reject(Integer code, String msg) {
        return new Result(code, msg, null);
    }
}
