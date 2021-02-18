package com.github.mrgzhen.gateway.swagger;

import com.github.mrgzhen.gateway.YanyuGatewayProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author yanyu
 */
@Configuration
@ConditionalOnProperty(value = "yanyu.gateway.swagger.locator.enabled")
public class SwaggerGatewayConfiguration {

    @Bean
    @ConditionalOnMissingBean(SwaggerRouteRule.class)
    public SwaggerRouteRule swaggerRouteRule() {
        return new SwaggerRouteRule();
    }

    @Bean
    @Primary
    public SwaggerResourceHold swaggerRouteHold(GatewayProperties gatewayProperties, SwaggerRouteRule swaggerRouteRule) {
        return new SwaggerResourceHold(gatewayProperties, swaggerRouteRule);
    }

    @Bean
    public DiscoveryClientSwaggerRouteLocator discoveryClientSwaggerRouteLocator(
            DiscoveryClient discoveryClient, SwaggerRouteRule swaggerRouteRule,
            SwaggerResourceHold swaggerRouteHold, YanyuGatewayProperties gatewayProperties) {
        return new DiscoveryClientSwaggerRouteLocator(discoveryClient, swaggerRouteRule, swaggerRouteHold, gatewayProperties);
    }

    @Bean
    public SwaggerProvider skySwaggerProvider(RouteLocator routeLocator, SwaggerResourceHold swaggerRouteHold) {
        return new SwaggerProvider(routeLocator, swaggerRouteHold);
    }

    @Bean
    public SwaggerEndpoint skySwaggerEndpoint(SwaggerProvider skySwaggerProvider) {
        return new SwaggerEndpoint(skySwaggerProvider);
    }
}
