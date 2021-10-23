package com.blcheung.missyou.kit;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.configuration.ExceptionCodeConfiguration;
import com.blcheung.missyou.exception.business.Failed;
import com.blcheung.missyou.exception.business.Success;
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

    public static <T> Result<T> resolve(T data, String msg) {
        return new Result<>(0, msg, data);
    }

    public static Result reject(String msg) {
        throw new Failed(msg);
    }

    public static void success(String msg) {
        throw new Success(msg);
    }

    public static void createSuccess() { throw new Success("创建成功", 201); }
}
