package com.github.mrgzhen.core.mybatis;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@EnablePluginRegistries(value = {SqlParserPlugin.class})
public class SqParserConfig {

    public SqParserConfig(@Qualifier("sqlParserPluginRegistry")
                                ObjectProvider<PluginRegistry<SqlParserPlugin, SqlParserRule>> parserPluginProvider) {
        SqlParserContext.init(parserPluginProvider.getIfAvailable());
    }
}
