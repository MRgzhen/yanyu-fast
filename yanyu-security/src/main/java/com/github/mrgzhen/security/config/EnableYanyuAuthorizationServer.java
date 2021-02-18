package com.github.mrgzhen.security.config;

import com.github.mrgzhen.security.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * 认证服务器启动类
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CustomAuthorizationServerSecurityConfiguration.class,
        CustomAuthenorizationEndpointConfiguration.class,
        CustomAuthorizationTokenGrantConfiguration.class,
        CustomWebSecurityConfiguration.class})
public @interface EnableYanyuAuthorizationServer {

    /**
     * 是否开启用户切换， false不开启， true开启
     */
    boolean switchUser() default false;
}