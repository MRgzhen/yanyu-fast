package com.github.mrgzhen.security.expection.handler;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.web.Result;
import com.github.mrgzhen.core.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未认证处理
 * @author yanyu
 */
@Slf4j
@Component
public class GeneralAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private GeneralErrorAttributesResolver errorAttributesResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        log.error("请求地址：[{}]，[{}异常：{}]",request.getRequestURI(), authException.getClass(), authException.getMessage(), authException);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if(authException instanceof InsufficientAuthenticationException) {
            response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.fail(new AuthenticationException())));
        } else {
            response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.fail(new AuthenticationException(authException.getMessage()))));
        }

    }
}
