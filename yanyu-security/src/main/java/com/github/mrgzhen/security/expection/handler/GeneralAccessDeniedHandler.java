package com.github.mrgzhen.security.expection.handler;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.PermissionException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.web.Result;
import com.github.mrgzhen.core.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未授权处理
 * @author yanyu
 */
@Slf4j
@Component
public class GeneralAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private GeneralErrorAttributesResolver errorAttributesResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("请求地址：[{}]，[{}异常：{}]",request.getRequestURI(), accessDeniedException.getClass(),
                accessDeniedException.getMessage(), accessDeniedException);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.fail(new PermissionException(accessDeniedException.getMessage()))));
    }
}
