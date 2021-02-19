package com.github.mrgzhen.security.expection.handler;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.PermissionException;
import com.github.mrgzhen.core.exception.ServiceException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.web.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 * @author yanyu
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.github.mrgzhen.security.controller")
@AllArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityExceptionHandler {

    @Autowired
    private GeneralErrorAttributesResolver errorAttributesResolver;

    @ExceptionHandler(OAuth2Exception.class)
    public Result handleOAuth2Exception(OAuth2Exception e, HttpServletRequest request) {
        log.error("[{}异常：{}]",e.getClass(), e.getMessage(),e);
        return Result.fail(new AuthenticationException(e.getMessage(), e));
    }

    @ExceptionHandler({org.springframework.security.core.AuthenticationException.class})
    public Result handleAuthenticationException(org.springframework.security.core.AuthenticationException e, HttpServletRequest request) {
        log.error("[{}异常：{}]", new Object[]{e.getClass(), e.getMessage(), e});
        return Result.fail(new AuthenticationException(e.getMessage(), e));
    }

    @ExceptionHandler({AccessException.class})
    public Result handleAccessException(AccessException e, HttpServletRequest request) {
        log.error("[{}异常：{}]", new Object[]{e.getClass(), e.getMessage(), e});
        return Result.fail(new PermissionException(e));
    }
}
