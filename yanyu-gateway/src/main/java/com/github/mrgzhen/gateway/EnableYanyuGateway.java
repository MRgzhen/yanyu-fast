package com.github.mrgzhen.gateway;

import com.github.mrgzhen.gateway.exception.EnableGeneralException;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({YanyuGatewayConfiguration.class})
@EnableGeneralException
public @interface EnableYanyuGateway {
}
