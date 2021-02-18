package com.github.mrgzhen.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.Map;

/**
 * @author yanyu
 */
@Slf4j
public class GeneralErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    private GeneralErrorAttributesResolver resolver;
    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     */
    public GeneralErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                           ErrorProperties errorProperties, ApplicationContext applicationContext,GeneralErrorAttributesResolver resolver) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        this.resolver = resolver;
    }

    /**
     * 定义RouterFunctions，任何请求都走renderErrorResponse处理
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK.value();
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.warn("系统异常,{}",resolver.getErrorAttributes(request));
        return objectMapper.convertValue(Result.fail(new GeneralException("503")),Map.class);
    }
}
