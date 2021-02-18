package com.github.mrgzhen.gateway;

import com.github.mrgzhen.core.CoreConfiguration;
import com.github.mrgzhen.core.spring.AppEnv;
import com.github.mrgzhen.gateway.exception.EnableGeneralException;
import com.github.mrgzhen.gateway.swagger.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

/**
 * @author yanyu
 */
@Configuration
@EnableConfigurationProperties({YanyuGatewayProperties.class})
@Import({SwaggerGatewayConfiguration.class, CoreConfiguration.class})
public class YanyuGatewayConfiguration {
    @Configuration
    @Slf4j
    public static class GatewayLogPrint implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            Boolean enabled = AppEnv.getBoolean("yanyu.gateway.swagger.locator.enabled");
            if(enabled != null && enabled) {
                log.info("自动注入swagger路由规则被开启");
            } else {
                log.info("自动注入swagger路由规则被关闭");
            }
        }
    }
}
