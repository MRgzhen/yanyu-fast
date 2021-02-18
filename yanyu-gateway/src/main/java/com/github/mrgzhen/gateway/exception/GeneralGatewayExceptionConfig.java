package com.github.mrgzhen.gateway.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * @author yanyu
 */
@Configuration
@AllArgsConstructor
public class GeneralGatewayExceptionConfig {

    private ServerProperties serverProperties;
    private ApplicationContext applicationContext;
    private ResourceProperties resourceProperties;
    private List<ViewResolver> viewResolvers;
    private ServerCodecConfigurer serverCodecConfigurer;

    @Bean
    public GeneralErrorAttributesResolver generalErrorAttributesResolver(ErrorAttributes errorAttributes) {
        return new GeneralErrorAttributesResolver(errorAttributes,this.serverProperties.getError());
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler customErrorWebExceptionHandler(ErrorAttributes errorAttributes, GeneralErrorAttributesResolver resolver) {
        GeneralErrorWebExceptionHandler exceptionHandler = new GeneralErrorWebExceptionHandler(
                errorAttributes, this.resourceProperties,
                this.serverProperties.getError(), this.applicationContext, resolver);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
