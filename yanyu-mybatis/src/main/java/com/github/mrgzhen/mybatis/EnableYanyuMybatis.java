package com.github.mrgzhen.mybatis;

import com.github.mrgzhen.core.CoreConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yanyu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MybatisConfiguration.class})
public @interface EnableYanyuMybatis {
}
