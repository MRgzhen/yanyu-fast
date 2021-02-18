package com.github.mrgzhen.core.mybatis;

import com.github.mrgzhen.core.exception.MybatisParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.plugin.core.PluginRegistry;

import java.util.List;

/**
 * @author yanyu
 */
@Slf4j
public class SqlParserContext {

    private static final ThreadLocal<SqlParser> PARSER_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<SqlParserRule> PARSER_RULE_HOLDER = new ThreadLocal<>();

    private static PluginRegistry<SqlParserPlugin, SqlParserRule> parserPlugins;

    public static void init(PluginRegistry<SqlParserPlugin, SqlParserRule> parserPlugins) {
        SqlParserContext.parserPlugins = parserPlugins;
    }

    public static SqlParser builder() {
        return new SqlParser();
    }

    public static SqlParserRule builderRule() {
        return new SqlParserRule();
    }

    public static SqlParser getParser() {
        if(PARSER_HOLDER.get() != null) {
            return PARSER_HOLDER.get();
        } else {
            SqlParserRule rule = PARSER_RULE_HOLDER.get();
            if(PARSER_RULE_HOLDER.get() == null) {
                log.warn("不能获取解析规则");
                throw new MybatisParserException("获取解析规则异常");
            }

            List<SqlParserPlugin> plugins = parserPlugins.getPlugins();
            for(SqlParserPlugin plugin : plugins) {
                if(plugin.supports(rule)){
                    rule.parser(plugin.build(rule));
                }
            }
            if(rule.getParser() == null) {
                throw new MybatisParserException("获取解析信息错误");
            }
            PARSER_HOLDER.set(rule.getParser());
            return rule.getParser();

        }
    }
    public static void removeParser() {
        PARSER_HOLDER.remove();
        PARSER_RULE_HOLDER.remove();
    }

    static void setParser(SqlParser parser) {
        PARSER_HOLDER.set(parser);
    }

    static void setParser(SqlParserRule rule) {
        PARSER_RULE_HOLDER.set(rule);
    }
}
