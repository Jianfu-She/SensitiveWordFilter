package org.sjf.demo.controller;

import org.sjf.demo.dto.JsonData;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 全局异常处理
 * Created by SJF on 2017/6/13.
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData exceptionHandler(Exception ex) {
        if (ex instanceof IOException)
            return new JsonData<>(1, "IO异常", null);
        if (ex instanceof HttpRequestMethodNotSupportedException)
            return new JsonData<>(1,"您的访问方式有误",null);
        if (ex instanceof MissingServletRequestParameterException)
            return new JsonData<>(1,"请查看您的参数列表",null);
        return new JsonData<>(1, "未知异常", null);
    }
}
