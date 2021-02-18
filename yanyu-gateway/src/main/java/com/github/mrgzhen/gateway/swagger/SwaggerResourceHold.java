package com.github.mrgzhen.gateway.swagger;

import com.github.mrgzhen.gateway.YanyuGatewayProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.*;

/**
 * @author yanyu
 */
public class SwaggerResourceHold implements InitializingBean {

    /** swagger信息 */
    public static final String API_URI = "/v2/api-docs";
    /** 版本号 */
    private static final String SWAGGER_VERSION = "2.0";
    private List<RouteDefinition> routeDefinitions;
    private Map<String, SwaggerResource> propertiesresourceMap = new HashMap<>();
    private GatewayProperties gatewayProperties;
    private SwaggerRouteRule swaggerRouteRule;

    public SwaggerResourceHold(GatewayProperties gatewayProperties, SwaggerRouteRule swaggerRouteRule) {
        this.gatewayProperties = gatewayProperties;
        this.swaggerRouteRule = swaggerRouteRule;
    }

    public void setRouteDefinition(List<RouteDefinition> routeDefinitions) {
        this.routeDefinitions = routeDefinitions;
    }

    public List<RouteDefinition> routeDefinitions() {
        return CollectionUtils.isNotEmpty(routeDefinitions) ? new ArrayList<>(this.routeDefinitions) : Collections.emptyList();
    }

    public Map<String, SwaggerResource> propertiesresourceMap() {
        return MapUtils.unmodifiableMap(this.propertiesresourceMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> swaggerRouteRule.assertRule(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            propertiesresourceMap.putIfAbsent(routeDefinition.getId(),
                                    swaggerResource(routeDefinition.getId(),predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")));
                        }));
    }

    public SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(StringUtils.lowerCase(StringUtils.substringBefore(name, SwaggerRouteRule.SWAGGER_ROUTE_SUFFIX)));
        swaggerResource.setLocation(location.replace("/**", API_URI));
        swaggerResource.setSwaggerVersion(SWAGGER_VERSION);
        return swaggerResource;
    }
}
