package com.github.mrgzhen.security.switchUser.handler;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.util.JSONUtil;
import com.github.mrgzhen.security.switchUser.matcher.SwitchUserRequestMatcher;
import com.github.mrgzhen.security.switchUser.resolver.SwitchUserAuthenticationResolver;
import com.github.mrgzhen.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 切换用户认证失败处理类
 * @author yanyu
 */
@Slf4j
public class SwitchUserAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private SwitchUserAuthenticationResolver resolver = new SwitchUserAuthenticationResolver();
    private SwitchUserRequestMatcher matcher;
    private GeneralErrorAttributesResolver errorAttributesResolver;

    public SwitchUserAuthenticationFailureHandler(SwitchUserRequestMatcher matcher, GeneralErrorAttributesResolver errorAttributesResolver) {
        this.matcher = matcher;
        this.errorAttributesResolver = errorAttributesResolver;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        log.warn("请求地址：{}，切换用户异常：{}",request.getRequestURI(),exception.getMessage());
        if(matcher.requiresSwitchUser(request)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(JSONUtil.instant().writeValueAsString(
                    Result.fail(new AuthenticationException("用户切换失败"), errorAttributesResolver.getErrorAttributes(request))));
        } else if(matcher.requiresExitUser(request)){
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(JSONUtil.instant().writeValueAsString(
                    Result.fail(new AuthenticationException("用户切换失败"), errorAttributesResolver.getErrorAttributes(request))));
        }
    }

    private static RequestMatcher createMatcher(String pattern) {
        return new AntPathRequestMatcher(pattern, "POST", true, new UrlPathHelper());
    }
}
