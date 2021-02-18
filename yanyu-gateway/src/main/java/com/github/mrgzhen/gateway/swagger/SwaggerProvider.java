package com.github.mrgzhen.gateway.swagger;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yanyu
 */
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;
    private final SwaggerResourceHold swaggerRouteHold;

    @Override
    public List<SwaggerResource> get() {
        Map<String,SwaggerResource> swaggerResourceMap = new HashMap<>();

        // 服务发现路由规则
        List<RouteDefinition> routeDefinitions = swaggerRouteHold.routeDefinitions();
        if(CollectionUtils.isNotEmpty(routeDefinitions)) {
            routeDefinitions.stream().forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                    .forEach(predicateDefinition -> {
                        swaggerResourceMap.putIfAbsent(routeDefinition.getId(),
                                swaggerRouteHold.swaggerResource(routeDefinition.getId(),
                                        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")));
                    }));
        }

        // 配置文件路由规则
        Map<String,SwaggerResource> defaultSwaggerResourceMap = swaggerRouteHold.propertiesresourceMap();
        defaultSwaggerResourceMap.entrySet().stream().forEach(entry -> {
            swaggerResourceMap.putIfAbsent(entry.getKey(),entry.getValue());
        });

        return swaggerResourceMap.values().stream().collect(Collectors.toList());
    }
}
