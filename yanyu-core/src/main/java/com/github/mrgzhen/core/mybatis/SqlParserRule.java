package com.github.mrgzhen.core.mybatis;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

/**
 * @author yanyu
 */
@Getter
public class SqlParserRule extends HashMap {

    private SqlParser parser;

    public SqlParserRule parser(SqlParser parser) {
        this.parser = parser;
        return this;
    }

    public SqlParserRule rule(String key, Object rule) {
        this.put(key, rule);
        return this;
    }

    public void hold() {
        SqlParserContext.setParser(this);
    }
}
