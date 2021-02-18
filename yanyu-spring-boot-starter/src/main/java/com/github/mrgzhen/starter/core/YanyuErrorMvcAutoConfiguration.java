package com.github.mrgzhen.starter.core;

import com.github.mrgzhen.core.exception.config.GeneralExceptionConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yanyu
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@Import(GeneralExceptionConfiguration.class)
public class YanyuErrorMvcAutoConfiguration {
}
