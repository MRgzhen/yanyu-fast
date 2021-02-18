package com.github.mrgzhen.security.config;

import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableResourceServer
@Import({CustomResourceServerTokenServiceConfiguration.class,
        CustomWebSecurityConfiguration.class})
public @interface EnableYanyuResourceServer {
}
