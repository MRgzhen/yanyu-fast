package com.github.mrgzhen.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.exception.ServiceException;
import com.github.mrgzhen.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author yanyu
 */
@Slf4j
public class GeneralErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    private GeneralErrorAttributesResolver resolver;
    public GeneralErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                           ErrorProperties errorProperties, ApplicationContext applicationContext,GeneralErrorAttributesResolver resolver) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        this.resolver = resolver;
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Result error = getResult(request);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }

    protected Result getResult(ServerRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String errorMsg = resolver.getMessage(request);
        HttpStatus httpStatus = resolver.getStatus(request);
        log.warn("状态码：[{}], 当前请求路径：[{}], 异常明细:[{}]",httpStatus.value(),request.uri(), errorMsg);
        if(StringUtils.isNoneBlank(errorMsg)) {
            return Result.fail(new GeneralException(String.valueOf(httpStatus.value()), errorMsg));
        } else {
            return Result.fail(new GeneralException(String.valueOf(httpStatus.value())));
        }
    }

    protected int getStatus(HttpStatus httpStatus) {
        if(httpStatus != null) {
            return  httpStatus.value();
        } else {
            return  HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
}
