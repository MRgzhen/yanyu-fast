package com.github.mrgzhen.core;

import com.github.mrgzhen.core.mybatis.SqParserConfig;
import com.github.mrgzhen.core.spring.AppContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yanyu
 */
@Configuration
@Import({AppContext.class, SqParserConfig.class})
public class CoreConfiguration {

}
