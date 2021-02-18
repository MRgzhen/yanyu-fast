package com.github.mrgzhen.gateway.swagger;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;

/**
 * @author yanyu
 */
public class SwaggerRouteRule {

    /** 路由id后缀 */
    public static final String SWAGGER_ROUTE_SUFFIX = "-SWAGGER";
    /** 服务前缀，断言路径规则将去除APPLICATION_NAME_PREFIX前缀 */
    public static final String APPLICATION_NAME_PREFIX = "";
    /** 路径断言参数，断言路径规则将增加PATH_PARAM配置 */
    public static final String SWAGGER_PATH_PARAM = "swagger";
    /** 负载均衡前缀 */
    public static final String LOAD_BALANCE_PREFIX = "lb://";

    /**
     * 路由id生成规则，upperCase('serviceId')-SWAGGER;
     */
    protected String routeId(String serviceId) {
        return StringUtils.join(StringUtils.upperCase(serviceId), SWAGGER_ROUTE_SUFFIX);
    }

    /**
     * 路由uri
     */
    protected URI uri(String serviceId) {
        return URI.create(StringUtils.join(LOAD_BALANCE_PREFIX,serviceId));
    }

    /**
     * 路由断言路径生成规则 Path = /swagger/'serviceId'/**
     */
    protected String predicatePath(String serviceId) {
        String path = StringUtils.join("/",SWAGGER_PATH_PARAM,"/",serviceId, "/**");
        return StringUtils.join("Path=",path);
    }

    /**
     * 路由过滤器
     */
    protected String filter() {
        return "StripPrefix=2";
    }

    /**
     * 路由是否符合规则
     */
    protected boolean assertRule(String id) {
        return id.endsWith(SwaggerRouteRule.SWAGGER_ROUTE_SUFFIX);
    }
}
