package com.github.mrgzhen.mybatis.parser;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface SqlParser {

    /** 数据权限解析 */
    boolean ds() default true;

    /** 租户解析 */
    boolean tenant() default true;
}
