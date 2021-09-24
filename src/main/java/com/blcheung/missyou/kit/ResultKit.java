package com.blcheung.missyou.kit;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.configuration.ExceptionCodeConfiguration;
import com.blcheung.missyou.exception.CreateSuccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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

    public static Result message(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public static void createSuccess() { throw new CreateSuccess(0); }
}
