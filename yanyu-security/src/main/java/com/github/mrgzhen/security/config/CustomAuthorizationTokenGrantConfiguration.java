package com.github.mrgzhen.security.config;


import com.github.mrgzhen.security.tokenGranter.SmsTokenGranter;
import com.github.mrgzhen.security.tokenGranter.SocialTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomAuthorizationTokenGrantConfiguration {

    @Configuration
    public static class AuthenticationTokenGrantConfigure {
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private ClientDetailsService clientDetailsService;

        public CompositeTokenGranter tokenGrant(AuthorizationServerEndpointsConfigurer endpoints) {

            AuthorizationServerTokenServices tokenService = endpoints.getTokenServices();
            OAuth2RequestFactory oAuth2RequestFactory = endpoints.getOAuth2RequestFactory();
            AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
            List<TokenGranter> tokenGranters = new ArrayList<>();
            // 这里配置密码模式
            if (authenticationManager != null) {
                tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenService, clientDetailsService, oAuth2RequestFactory));
                //自定义手机号验证码模式、
                tokenGranters.add(new SmsTokenGranter(authenticationManager, tokenService, clientDetailsService, oAuth2RequestFactory));
                //自定义第三方认证模式、
                tokenGranters.add(new SocialTokenGranter(authenticationManager, tokenService, clientDetailsService, oAuth2RequestFactory));
            }
            //刷新token模式、
            tokenGranters.add(new RefreshTokenGranter(tokenService, clientDetailsService, oAuth2RequestFactory));
            //授权码模式、
            tokenGranters.add(new AuthorizationCodeTokenGranter(tokenService, authorizationCodeServices, clientDetailsService, oAuth2RequestFactory));
            //、简化模式
            tokenGranters.add(new ImplicitTokenGranter(tokenService, clientDetailsService, oAuth2RequestFactory));
            //客户端模式
            tokenGranters.add(new ClientCredentialsTokenGranter(tokenService, clientDetailsService,  oAuth2RequestFactory));

            return new CompositeTokenGranter(tokenGranters);

        }
    }
}
