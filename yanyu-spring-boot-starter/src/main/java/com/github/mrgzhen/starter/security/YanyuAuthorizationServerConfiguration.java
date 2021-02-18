package com.github.mrgzhen.starter.security;

import com.github.mrgzhen.security.config.CustomAuthorizationServerSecurityConfiguration;
import com.github.mrgzhen.security.config.CustomAuthorizationTokenGrantConfiguration;
import com.github.mrgzhen.security.config.EnableYanyuAuthorizationServer;
import com.github.mrgzhen.security.constant.SecurityConstant;
import com.github.mrgzhen.security.generator.CustomAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author yanyu
 */
@Configuration
@ConditionalOnBean(CustomAuthorizationServerSecurityConfiguration.class)
public class YanyuAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private CustomAuthorizationTokenGrantConfiguration.AuthenticationTokenGrantConfigure tokenGrantConfigure;

    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore).tokenGranter(tokenGrantConfigure.tokenGrant(endpoints));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.authenticationEntryPoint(authenticationEntryPoint).tokenKeyAccess("isAuthenticated()");
    }

    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore() {
        InMemoryTokenStore tokenStore = new InMemoryTokenStore();
        tokenStore.setAuthenticationKeyGenerator(new CustomAuthenticationKeyGenerator());
        return tokenStore;
    }
}
