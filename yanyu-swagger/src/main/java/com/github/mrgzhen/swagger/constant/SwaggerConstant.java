package com.github.mrgzhen.swagger.constant;

/**
 * @author yanyu
 */
public interface SwaggerConstant {

    String[] SWAGGER_IGNORING_URL = new String[]{"/v2/api-docs", //swagger api json
            "/swagger-resources/configuration/ui",//用来获取支持的动作
            "/swagger-resources/**",//用来获取api-docs的URI
            "/swagger-resources/configuration/security",//安全选项
            "/webjars/**",
            "/swagger-ui.html"};
}
