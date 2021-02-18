package com.github.mrgzhen.security.config;



import com.github.mrgzhen.security.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configuration.ClientDetailsServiceConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.Collections;
import java.util.List;

/**
 * @author yanyu
 */
@Configuration
@Order(-1)
@Import({ ClientDetailsServiceConfiguration.class, AuthorizationServerEndpointsConfiguration.class })
public class CustomAuthorizationServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerEndpointsConfiguration endpoints;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAuthenorizationEndpointConfiguration.ValidCodeConfigure validCodeConfigure;

    @Autowired
    public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
        for (AuthorizationServerConfigurer configurer : configurers) {
            configurer.configure(clientDetails);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Over-riding to make sure this.disableLocalConfigureAuthenticationBldr = false
        // This will ensure that when this configurer builds the AuthenticationManager it will not attempt
        // to find another 'Global' AuthenticationManager in the ApplicationContext (if available),
        // and set that as the parent of this 'Local' AuthenticationManager.
        // This AuthenticationManager should only be wired up with an AuthenticationProvider
        // composed of the ClientDetailsService (wired in this configuration) for authenticating 'clients' only.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
        FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
        http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
        configure(configurer);
        http.apply(configurer);
        String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
        String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
        String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
        if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
            UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
            endpoints.getEndpointsConfigurer().userDetailsService(userDetailsService);
        }
        // @formatter:off
        http
                .authorizeRequests()
                .antMatchers(tokenEndpointPath).fullyAuthenticated()
                // 自定义url on
                .antMatchers(securityProperties.getAuth().getLoginUri()).fullyAuthenticated()
                .antMatchers(securityProperties.getAuth().getImageCodeUri()).fullyAuthenticated()
                .antMatchers(securityProperties.getAuth().getSmsLoginUri()).fullyAuthenticated()
                .antMatchers(securityProperties.getAuth().getSmsCodeUri()).fullyAuthenticated()
                .antMatchers(securityProperties.getAuth().getSocialLoginUri()).fullyAuthenticated()
                // 自定义url off
                .antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
                .antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
                .and()
                .requestMatchers()
                .antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath,
                        // 自定义url on
                        securityProperties.getAuth().getLoginUri(),
                        securityProperties.getAuth().getImageCodeUri(),
                        securityProperties.getAuth().getSmsLoginUri(),
                        securityProperties.getAuth().getSmsCodeUri(),
                        securityProperties.getAuth().getSocialLoginUri())
                        // 自定义url off
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                // 自定义basic返回值
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);
        // @formatter:on
        // 自定义，增加验证码校验
        http.apply(validCodeConfigure);
        http.setSharedObject(ClientDetailsService.class, clientDetailsService);
    }

    protected void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        for (AuthorizationServerConfigurer configurer : configurers) {
            configurer.configure(oauthServer);
        }
    }
}
