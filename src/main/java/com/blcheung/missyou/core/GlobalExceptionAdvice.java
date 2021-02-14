package com.blcheung.missyou.core;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

// 增强型Controller，可用于做全局处理相关操作；
// 是一个@Component，用于定义@ExceptionHandler，@InitBinder和@ModelAttribute方法；
// 适用于所有使用@RequestMapping方法；
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)   // 用于捕获异常注解
    public void handleException(HttpServletRequest request, Exception exception) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        System.out.println(exception.getMessage());
    }
}
