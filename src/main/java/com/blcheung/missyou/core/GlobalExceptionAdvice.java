package com.blcheung.missyou.core;

import com.blcheung.missyou.exception.http.HttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

// 增强型Controller，可用于做全局处理相关操作；
// 是一个@Component，用于定义@ExceptionHandler，@InitBinder和@ModelAttribute方法；
// 适用于所有使用@RequestMapping方法；
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)   // 用于捕获系统异常
    @ResponseBody   // 不加的话 会无法序列化当前方法返回的对象
    public GlobalResponseData handleException(HttpServletRequest request, Exception exception) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        System.out.println(exception.getMessage());
        return new GlobalResponseData(9999, "服务器异常", requestMethod + " " + requestURI);
    }

    @ExceptionHandler(HttpException.class)  // 用于捕获Http运行时异常
    public void handleHttpException(HttpServletRequest request, HttpException exception) {
        System.out.println(exception.getMessage());
    }
}
