package com.github.mrgzhen.security.mybatis;

import com.github.mrgzhen.core.mybatis.SqlParserContext;
import com.github.mrgzhen.core.mybatis.SqlParserRule;
import com.github.mrgzhen.core.security.LoginUserContext;
import com.github.mrgzhen.security.constant.SecurityConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanyu
 */
public class ParserHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SqlParserRule parserRule = SqlParserContext.builderRule().rule(SecurityConstant.REQUESTURL_SQL_PARSER_RULE_KEY, request.getRequestURI());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            parserRule.rule(SecurityConstant.AUTHENTICATED_SQL_PARSER_RULE_KEY, false);
        } else {
            parserRule.rule(SecurityConstant.AUTHENTICATED_SQL_PARSER_RULE_KEY, authentication.isAuthenticated())
                    .rule(SecurityConstant.AUTHENTICATION_SQL_PARSER_RULE_KEY, LoginUserContext.getLoginUser());
        }
        parserRule.hold();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SqlParserContext.removeParser();
    }
}
