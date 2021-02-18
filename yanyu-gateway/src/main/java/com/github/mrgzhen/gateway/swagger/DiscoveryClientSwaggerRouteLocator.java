package com.github.mrgzhen.gateway.swagger;

import com.github.mrgzhen.gateway.YanyuGatewayProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * swagger服务注册
 * @author yanyu
 */
@Slf4j
@AllArgsConstructor
public class DiscoveryClientSwaggerRouteLocator implements RouteDefinitionLocator {

    private static final String ALL_SERVICE = "*";
    private DiscoveryClient discoveryClient;
    private SwaggerRouteRule swaggerRouteRule;
    private SwaggerResourceHold swaggerRouteHold;
    private YanyuGatewayProperties gatewayProperties;

    public Flux<RouteDefinition> getRouteDefinitions() {
        if(ArrayUtils.isEmpty(gatewayProperties.getSwagger().getServiceIds())) {
            log.debug("yanyu.gateway.swagger.service-ids为空，自动注入swagger路由失败");
            return Flux.empty();
        }

        List<String> loadServiceIds = new ArrayList<>();
        List<RouteDefinition> routeDefinitions = new ArrayList<>();

        // 获取所有服务
        List<String> serviceIds = discoveryClient.getServices();
        List<String> actualServiceIds = Arrays.asList(gatewayProperties.getSwagger().getServiceIds());
        if(actualServiceIds.size() == 1 && ALL_SERVICE.equals(actualServiceIds.get(0))) {
            log.debug("自动注入swagger路由规则，配置文件：【{}】，服务发现：【{}】",
                    StringUtils.join(actualServiceIds,","), StringUtils.join(serviceIds,","));
            for(String serviceId : serviceIds) {
                routeDefinitions.add(routes(serviceId));
            }
        } else {
            List<String> mergeServiceIds = (List<String>) CollectionUtils.intersection(serviceIds, actualServiceIds);
            log.debug("自动注入swagger路由规则， 配置文件：【{}】，服务发现：【{}】，交集：【{}】",
                    StringUtils.join(actualServiceIds,","), StringUtils.join(serviceIds,","),  StringUtils.join(mergeServiceIds,","));
            for(String serviceId : mergeServiceIds) {
                loadServiceIds.add(serviceId);
                routeDefinitions.add(routes(serviceId));
            }
        }
        swaggerRouteHold.setRouteDefinition(routeDefinitions);
        return Flux.fromIterable(routeDefinitions);
    }

    public RouteDefinition routes(String serviceId) {
        // 注册服务
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(swaggerRouteRule.routeId(serviceId));
        routeDefinition.setUri(swaggerRouteRule.uri(serviceId));

        // 断言
        PredicateDefinition predicateDefinition = new PredicateDefinition(swaggerRouteRule.predicatePath(serviceId));
        routeDefinition.setPredicates(Arrays.asList(predicateDefinition));

        // 过滤器
        FilterDefinition filterDefinition = new FilterDefinition(swaggerRouteRule.filter());
        routeDefinition.setFilters(Arrays.asList(filterDefinition));
        return routeDefinition;
    }
}
