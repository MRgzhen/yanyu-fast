package com.github.mrgzhen.swagger;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

/**
 * swagger配置类
 * @author  yanyu
 */
@Configuration
@EnableSwagger2
@AllArgsConstructor
@ConditionalOnSwaggerEnabled
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerConfiguration {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private SwaggerProperties properties;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())// 文档信息
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))// 扫描的包
                .paths(PathSelectors.any())// 过滤的路径，所有请求
                .build()
                .securityContexts(Arrays.asList(securityContexts()))  // 认证上下文
                .securitySchemes(Arrays.asList(securitySchemes()));

    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(securityReferences())
                .forPaths(PathSelectors.any())
                .build();
    }

    private SecurityScheme securitySchemes() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope("**", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .contact(new Contact(properties.getContact().getName(),properties.getContact().getUrl(),properties.getContact().getEmail()))
                .description(properties.getDescription())
                .version(properties.getVersion())
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .build();
    }
}
