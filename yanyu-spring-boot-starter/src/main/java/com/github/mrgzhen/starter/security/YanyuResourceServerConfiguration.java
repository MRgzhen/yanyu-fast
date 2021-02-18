package com.github.mrgzhen.starter.security;


import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.config.EnableYanyuResourceServer;
import com.github.mrgzhen.security.switchUser.configure.SwitchSecurityConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author yanyu
 */
@Configuration
@EnableYanyuResourceServer
@AutoConfigureBefore(OAuth2AutoConfiguration.class)
public class YanyuResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private SecurityProperties properties;
    @Autowired
    private ObjectProvider<SwitchSecurityConfigurer> skySwitchSecurityConfigurerObjectProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(properties.getPermits()).permitAll()
                .anyRequest().authenticated();

        skySwitchSecurityConfigurerObjectProvider.ifAvailable(skySwitchSecurityConfigurer -> {
            try {
                http.apply(skySwitchSecurityConfigurer);
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(authenticationEntryPoint);
    }
}

