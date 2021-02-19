package com.github.mrgzhen.security.expection.handler;

import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.util.JSONUtil;
import com.github.mrgzhen.core.web.Result;
import com.github.mrgzhen.security.expection.handler.ValidCodeFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanyu
 */
@Slf4j
@Component
public class DefaultValidCodeFailureHandler implements ValidCodeFailureHandler {

    @Autowired
    private GeneralErrorAttributesResolver errorAttributesResolver;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, GeneralException exception) throws IOException, ServletException {
        log.error("请求地:[{}]，[{}异常：{}]",request.getRequestURI(), exception.getClass(), exception.getMessage(), exception);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.fail(exception)));
    }
}
