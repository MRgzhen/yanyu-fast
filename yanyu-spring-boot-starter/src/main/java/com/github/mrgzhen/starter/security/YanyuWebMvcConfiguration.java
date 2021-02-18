package com.github.mrgzhen.starter.security;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.github.mrgzhen.security.mybatis.AuthenticationParserPlugin;
import com.github.mrgzhen.security.mybatis.ParserHandlerInterceptorAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yanyu
 */
@Configuration
@Import(AuthenticationParserPlugin.class)
@ConditionalOnBean(MybatisPlusInterceptor.class)
public class YanyuWebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParserHandlerInterceptorAdapter())
                .addPathPatterns("/**");
    }
}
