package com.github.mrgzhen.gateway.exception;

import com.github.mrgzhen.core.exception.config.GeneralExceptionPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GeneralGatewayExceptionConfig.class})
@PropertySource(value = "exception",name="exception",
        factory = GeneralExceptionPropertySourceFactory.class, ignoreResourceNotFound=true)
public @interface EnableGeneralException {
}
