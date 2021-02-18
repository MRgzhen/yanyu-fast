package com.github.mrgzhen.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnSwaggerEnabled
@Import({SwaggerConfiguration.class})
public @interface EnableYanyuSwagger {
}
