package com.github.mrgzhen.security.valid.code;

import com.github.mrgzhen.core.exception.ServiceException;
import com.github.mrgzhen.security.expection.ValidCodeException;
import com.github.mrgzhen.security.expection.handler.DefaultValidCodeFailureHandler;
import com.github.mrgzhen.security.expection.handler.ValidCodeFailureHandler;
import com.github.mrgzhen.security.valid.code.service.ValidCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanyu
 */
@Slf4j
public abstract class AbstractValidCodeFilter extends GenericFilter {

    private AntPathRequestMatcher validMatcher;
    private AntPathRequestMatcher generatorValidMatcher;
    private ValidCodeService validCodeService;
    private ValidCodeFailureHandler validCodeFailureHandler;

    public AbstractValidCodeFilter(String validUri,
                                   String generatorUri,
                                   ValidCodeService validCodeService,
                                   ValidCodeFailureHandler validCodeFailureHandler) {
        this.validMatcher = new AntPathRequestMatcher(validUri);
        this.generatorValidMatcher = new AntPathRequestMatcher(generatorUri);
        this.validCodeService = validCodeService;
        this.validCodeFailureHandler = validCodeFailureHandler;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 不启用
        if(!enabled()) {
            chain.doFilter(req, res);
            return;
        }

        if(validMatcher.matches(request)) {
            try {
                validCodeService.validCode(new ServletWebRequest(request, response));
            } catch (ValidCodeException e) {
                log.info("验证码验证失败:[{}]",e.getMessage());
                validCodeFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            } catch (Exception e) {
                log.info("验证码验证过程抛出未知异常：[{}]",e.getMessage());
                validCodeFailureHandler.onAuthenticationFailure(request, response, new ServiceException("验证码配置异常",e));
                return;
            }
        } else if(generatorValidMatcher.matches(request)){
            try {
                validCodeService.generatorAndSend(new ServletWebRequest(request, response));
            } catch (Exception e) {
                log.info("生成并发送验证码处理逻辑抛出未知异常：[{}]",e.getMessage());
                validCodeFailureHandler.onAuthenticationFailure(request, response, new ServiceException("发送验证码异常",e));
            }
            return;
        }
        chain.doFilter(req, res);
    }

    protected void setValidCodeFailureHandler(ValidCodeFailureHandler validCodeFailureHandler) {
        this.validCodeFailureHandler = validCodeFailureHandler;
    }

    protected boolean enabled() {
        return true;
    }
}
