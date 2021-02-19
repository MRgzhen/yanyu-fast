package com.github.mrgzhen.core.exception.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.github.mrgzhen.core.exception.GeneralException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanyu
 */
@AllArgsConstructor
public class GeneralErrorAttributesResolver {

    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    public String getMessage(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        Throwable error = getError(webRequest);
        if (error != null && error instanceof GeneralException) {
            return error.getMessage();
        }
        return null;
    }

    public String getBindingResultErrorMessage(Throwable error) {
        BindingResult result = extractBindingResult(error);
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors().forEach((fieldError) -> {
            sb.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(",");
        });
        return sb.toString();
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private Throwable getError(WebRequest webRequest) {
        Throwable exception = getAttribute(webRequest, ERROR_ATTRIBUTE);
        return (exception != null) ? exception : getAttribute(webRequest, RequestDispatcher.ERROR_EXCEPTION);
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }


}
