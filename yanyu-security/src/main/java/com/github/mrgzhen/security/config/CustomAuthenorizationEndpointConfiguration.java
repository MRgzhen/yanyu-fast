package com.github.mrgzhen.security.config;


import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.controller.AuthController;
import com.github.mrgzhen.security.controller.LoginController;
import com.github.mrgzhen.security.expection.handler.ValidCodeFailureHandler;
import com.github.mrgzhen.security.valid.code.ComposeValidCodeProcessor;
import com.github.mrgzhen.security.valid.code.ImageValidCodeFilter;
import com.github.mrgzhen.security.valid.code.SmsValidCodeFilter;
import com.github.mrgzhen.security.valid.code.service.ValidCodeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

/**
 * @author yanyu
 */
@Configuration
public class CustomAuthenorizationEndpointConfiguration {

    @Bean
    public LoginController loginController() {
        return new LoginController();
    }
    @Bean
    public AuthController authMeController() {
        return new AuthController();
    }
    @Bean
    public ComposeValidCodeProcessor ComposeValidCodeProcessor(@Autowired List<ValidCodeService> validCodeServices) {
        return new ComposeValidCodeProcessor(validCodeServices);
    }

    @Configuration
    @AllArgsConstructor
    public static class ValidCodeConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

        private SecurityProperties properties;
        private ComposeValidCodeProcessor validCodeProcessor;
        private ValidCodeFailureHandler validCodeFailureHandler;

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            ImageValidCodeFilter imageValidCodeFilter = new ImageValidCodeFilter(properties, validCodeProcessor, validCodeFailureHandler);
            SmsValidCodeFilter smsValidCodeFilter = new SmsValidCodeFilter(properties, validCodeProcessor, validCodeFailureHandler);
            builder.addFilterBefore(imageValidCodeFilter, BasicAuthenticationFilter.class)
                    .addFilterBefore(smsValidCodeFilter, BasicAuthenticationFilter.class);
        }
    }
}
