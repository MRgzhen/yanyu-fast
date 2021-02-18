package com.github.mrgzhen.security.config;

import com.github.mrgzhen.core.spring.AppEnv;
import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.tokenGranter.authentication.*;
import com.github.mrgzhen.swagger.constant.SwaggerConstant;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author yanyu
 */
@Configuration
@ComponentScan(basePackages = CustomWebSecurityConfiguration.EXCEPTION_PACKAGES)
@EnableConfigurationProperties(value = {SecurityProperties.class})
public class CustomWebSecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {

    public static final String EXCEPTION_PACKAGES = "com.github.mrgzhen.security.expection.handler";

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private Environment env;
    @Autowired
    private ObjectProvider<SmsUserDetailsService> smsUsersDetailsService;
    @Autowired
    private ObjectProvider<SocialUserDetailService> socialUsersDetailsService;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] ignores = properties.getIgnores();
        Boolean swagger = env.getProperty("yanyu.swagger.enabled",Boolean.class);
        if(AppEnv.getBoolean("yanyu.swagger.enabled")) {
            ignores = ArrayUtils.addAll(ignores, SwaggerConstant.SWAGGER_IGNORING_URL);
        }
        web.ignoring().antMatchers(ignores);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authenticationProvider(new SmsAuthenticationProvider(smsUsersDetailsService.getIfAvailable()))
                .authenticationProvider(new SocialAuthenticationProvider(socialUsersDetailsService.getIfAvailable()))
                .authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
    }

    /**
     * 认证服务器需要
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 认证服务器需要
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

}


