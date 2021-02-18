package com.github.mrgzhen.core.mybatis;

import org.springframework.plugin.core.Plugin;

/**
 * @author yanyu
 */
public interface SqlParserPlugin extends Plugin<SqlParserRule> {

    SqlParser build(SqlParserRule rule);
}
