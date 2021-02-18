package com.github.mrgzhen.core.exception.config;

import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.core.exception.handler.GeneralErrorController;
import com.github.mrgzhen.core.exception.handler.GeneralExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author yanyu
 */
@Configuration
@PropertySource(value = "exception",name="exception",
        factory = GeneralExceptionPropertySourceFactory.class, ignoreResourceNotFound=true)
public class GeneralExceptionConfiguration {

    @Bean
    public GeneralErrorAttributesResolver generalErrorAttributesWrapper(ServerProperties serverProperties) {
        return new GeneralErrorAttributesResolver(serverProperties.getError());
    }

    @Bean
    public ErrorController generalErrorController(GeneralErrorAttributesResolver errorAttributesHandler) {
        return new GeneralErrorController(errorAttributesHandler);
    }

    @Bean
    public GeneralExceptionHandler generalExceptionHandler(GeneralErrorAttributesResolver errorAttributesHandler) {
        return new GeneralExceptionHandler(errorAttributesHandler);
    }
}
