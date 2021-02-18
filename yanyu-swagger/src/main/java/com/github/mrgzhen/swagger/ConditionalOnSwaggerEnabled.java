package com.github.mrgzhen.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ConditionalOnProperty(value = "yanyu.swagger.enabled")
public @interface ConditionalOnSwaggerEnabled {
}
