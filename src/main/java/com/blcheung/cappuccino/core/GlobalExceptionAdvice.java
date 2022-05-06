package com.blcheung.cappuccino.core;

import com.blcheung.cappuccino.core.configuration.ExceptionCodeConfiguration;
import com.blcheung.cappuccino.exception.business.BusinessException;
import com.blcheung.cappuccino.exception.http.HttpException;
import com.blcheung.cappuccino.common.ErrorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

// 增强型Controller，可用于做全局处理相关操作；
// 是一个@Component，用于定义@ExceptionHandler，@InitBinder和@ModelAttribute方法；
// 适用于所有使用@RequestMapping方法；
@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ExceptionCodeConfiguration codeConfiguration;

    @ExceptionHandler(Exception.class)   // 用于捕获系统异常
    @ResponseBody   // 不加的话 会无法序列化当前方法返回的对象
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)    // 调整状态码为500，否则会是200
    public ErrorResult handleException(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        System.out.println(e.getMessage());

        // return new GlobalResponseData(9999, "服务器异常", requestMethod + " " + requestURI);
        return new ErrorResult(9999, codeConfiguration.getMessage(9999), requestMethod + " " + requestURI);
    }


    /**
     * 用于捕获Http运行时异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResult> handleHttpException(HttpServletRequest request, HttpException e) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        System.out.println(e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpStatus status = new HttpStatus();
        HttpStatus httpStatus = HttpStatus.resolve(e.getStatusCode());

        ErrorResult errorResult = new ErrorResult(e.getCode(), codeConfiguration.getMessage(e.getCode()),
                                                  requestMethod + " " + requestURI);
        assert httpStatus != null;
        return new ResponseEntity<>(errorResult, headers, httpStatus);
    }

    /**
     * 用于捕获因业务需要产生的异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResult> handleBusinessException(HttpServletRequest request, BusinessException e) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        System.out.println(e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpStatus httpStatus = HttpStatus.resolve(e.getStatusCode());

        ErrorResult errorResult = new ErrorResult(e.getCode(), e.getMsg(), requestMethod + " " + requestURI);

        assert httpStatus != null;
        return new ResponseEntity<>(errorResult, headers, httpStatus);
    }


    /**
     * 捕捉通过body方式的参数验证错误抛出的异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResult handleBeanValidatorException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();


        List<ObjectError> errors = e.getBindingResult()
                                    .getAllErrors();

        String message = this.formatAllBeanValidatorErrorMessage(errors);

        // return new GlobalResponseData(10001, message, requestMethod + " " + requestURI);
        return new ErrorResult(10001, message, requestMethod + " " + requestURI);
    }


    /**
     * 捕捉通过query方式参数验证错误抛出的异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResult handleConstraintException(HttpServletRequest request, ConstraintViolationException e) {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        //        String message = e.getMessage();    // 这是Exception的message，已拼接好的
        String message = this.formatAllConstraintViolationErrorMessage(e);

        // return new GlobalResponseData(10001, message, requestMethod + " " + requestURI);
        return new ErrorResult(10001, message, requestMethod + " " + requestURI);
    }


    /**
     * 格式化body方式的参数验证错误
     *
     * @param errors
     * @return
     */
    private String formatAllBeanValidatorErrorMessage(List<ObjectError> errors) {
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(error -> errorMsg.append(error.getDefaultMessage())
                                        .append(";"));
        return errorMsg.toString();
    }


    /**
     * 格式化query方式参数验证错误
     *
     * @param e
     * @return
     */
    private String formatAllConstraintViolationErrorMessage(ConstraintViolationException e) {
        StringBuffer errorMsg = new StringBuffer();
        e.getConstraintViolations()
         .forEach(err -> {
             String[] propertyPath = err.getPropertyPath()
                                        .toString()
                                        .split("\\.");
             errorMsg.append(propertyPath[ propertyPath.length - 1 ])
                     .append(err.getMessage())
                     .append(";");
         });
        return errorMsg.toString();
    }
}
