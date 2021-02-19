package com.github.mrgzhen.security.switchUser.handler;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.util.JSONUtil;
import com.github.mrgzhen.security.switchUser.matcher.SwitchUserRequestMatcher;
import com.github.mrgzhen.security.switchUser.resolver.SwitchUserAuthenticationResolver;
import com.github.mrgzhen.security.switchUser.tokenEnhancer.SwitchUserAccessTokenEnhancer;
import com.github.mrgzhen.core.web.Result;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 切换用户认证成功处理类
 * @author yanyu
 */
@Setter
@Slf4j
@NoArgsConstructor
public class SwitchUserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private SwitchUserAccessTokenEnhancer tokenEnhancer = new SwitchUserAccessTokenEnhancer();
    private SwitchUserAuthenticationResolver resolver = new SwitchUserAuthenticationResolver();
    private SwitchUserRequestMatcher matcher;
    private AuthorizationServerTokenServices tokenService;
    private GeneralErrorAttributesResolver errorAttributesResolver;

    public SwitchUserAuthenticationSuccessHandler(AuthorizationServerTokenServices tokenService, SwitchUserRequestMatcher matcher,
                                                  GeneralErrorAttributesResolver errorAttributesResolver) {
        this.tokenService = tokenService;
        this.matcher = matcher;
        this.errorAttributesResolver = errorAttributesResolver;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2Authentication original = null;
            OAuth2Authentication targetAuth2Authentication = null;
            OAuth2AccessToken accessToken = null;
            if(matcher.requiresSwitchUser(request)) {
                original = (OAuth2Authentication)resolver.getSourceAuthentication(authentication);

                // 重新设置凭证
                OAuth2Request oAuth2Request = original.getOAuth2Request();
                targetAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

                // 创建token
                accessToken = tokenService.createAccessToken(targetAuth2Authentication);
                tokenEnhancer.enhance(accessToken);
            } else  if(matcher.requiresExitUser(request)){
                original = (OAuth2Authentication)authentication;

                // 重新设置凭证
                OAuth2Request oAuth2Request = original.getOAuth2Request();
                Authentication originalAuth = ((OAuth2Authentication) authentication).getUserAuthentication();
                targetAuth2Authentication = new OAuth2Authentication(oAuth2Request, originalAuth);

                // 创建token
                accessToken = tokenService.createAccessToken(targetAuth2Authentication);
                tokenEnhancer.inhance(accessToken);
            }

            // 设置凭证
            SecurityContextHolder.getContext().setAuthentication(targetAuth2Authentication);

            // 响应
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.success(accessToken)));
        } catch(AuthenticationException e) {
            // 响应
            log.warn("请求地址:{}，认证失败：{}",request.getRequestURI(),e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(JSONUtil.instant().writeValueAsString(Result.fail(e)));
        }
    }
}
