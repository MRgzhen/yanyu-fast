package com.github.mrgzhen.core.exception.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.github.mrgzhen.core.exception.support.ErrorResult;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
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

    private ErrorProperties errorProperties;
    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    public ErrorResult getErrorAttributes(HttpServletRequest request, Throwable throwable) {
        ErrorResult errorResult = getErrorAttributes(request, throwable, true);
        return wrap(errorResult);
    }

    public ErrorResult getErrorAttributes(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        ErrorResult errorResult = getErrorAttributes(webRequest, true);
        return wrap(errorResult);
    }

    public ErrorResult getAllErrorAttributes(HttpServletRequest request, Throwable throwable) {
        return getErrorAttributes(request, throwable, true);
    }

    public ErrorResult getAllErrorAttributes(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        return getErrorAttributes(webRequest, true);
    }

    private ErrorResult wrap(ErrorResult errorResult) {
        if(errorProperties.getIncludeStacktrace().equals(ErrorProperties.IncludeStacktrace.NEVER)) {
            errorResult.setTrace(null);
        }
        if(errorProperties.getIncludeMessage().equals(ErrorProperties.IncludeAttribute.NEVER)) {
            errorResult.setMsg(null);
        }
        if(!errorProperties.isIncludeException()) {
            errorResult.setException(null);
        }
        return errorResult;
    }

    private ErrorResult getErrorAttributes(HttpServletRequest request, Throwable throwable, boolean includeStackTrace) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setPath(request.getRequestURI());
        errorResult.setTimestamp(new Date());
        addErrorDetails(errorResult, throwable, includeStackTrace);
        return errorResult;
    }
    private ErrorResult getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        ErrorResult errorResult = new ErrorResult();
        addPath(errorResult, webRequest);
        errorResult.setTimestamp(new Date());
        addErrorDetails(errorResult, webRequest, includeStackTrace);
        return errorResult;
    }

    private void addPath(ErrorResult errorResult, WebRequest webRequest) {
        String path = getAttribute(webRequest, RequestDispatcher.ERROR_REQUEST_URI);
        if (path != null) {
            errorResult.setPath(path);
        }
    }

    private void addErrorDetails(ErrorResult errorResult, Throwable error, boolean includeStackTrace) {
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            errorResult.setException(error.getClass().getName());
            if (includeStackTrace) {
                addStackTrace(errorResult, error);
            }
        }

        // 设置message
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            errorResult.setMsg(error.toString());
        }
        else {
            addBindingResultErrorMessage(errorResult, result);
        }
    }

    private void addErrorDetails(ErrorResult errorResult, WebRequest webRequest,
                                 boolean includeStackTrace) {
        Throwable error = getError(webRequest);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            errorResult.setException(error.getClass().getName());
            if (includeStackTrace) {
                addStackTrace(errorResult, error);
            }
        }

        // 设置message
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            addExceptionErrorMessage(errorResult, webRequest, error);
        }
        else {
            addBindingResultErrorMessage(errorResult, result);
        }
    }

    public Throwable getError(WebRequest webRequest) {
        Throwable exception = getAttribute(webRequest, ERROR_ATTRIBUTE);
        return (exception != null) ? exception : getAttribute(webRequest, RequestDispatcher.ERROR_EXCEPTION);
    }

    private void addStackTrace(ErrorResult errorResult, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorResult.setTrace(stackTrace.toString());
    }

    private void addExceptionErrorMessage(ErrorResult errorResult, WebRequest webRequest, Throwable error) {
        Object message = getAttribute(webRequest, RequestDispatcher.ERROR_MESSAGE);
        if (StringUtils.isEmpty(message) && error != null) {
            message = error.getMessage();
        }
        if (StringUtils.isEmpty(message)) {
            message = "No message available";
        }
        errorResult.setMsg(message.toString());
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

    private void addBindingResultErrorMessage(ErrorResult errorResult, BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors().forEach((fieldError) -> {
            sb.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(",");
        });
        errorResult.setMsg(sb.toString());
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }


}
