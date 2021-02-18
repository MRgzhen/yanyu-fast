package com.github.mrgzhen.mybatis.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.github.mrgzhen.core.mybatis.SqlParser;
import com.github.mrgzhen.core.mybatis.SqlParserContext;
import com.github.mrgzhen.mybatis.MybatisProperties;
import com.github.mrgzhen.mybatis.parser.SqlParserHelper;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * 排除数据库表，不在该表后拼接租户标识
 * @author yanyu
 */
public class GeneralTenantHandler implements TenantLineHandler {

    public boolean ignoreMethod() {
        return !SqlParserHelper.getSqlParserAttributeHolder().isTenant();
    }

    @Override
    public Expression getTenantId() {
        SqlParser parser = SqlParserContext.getParser();
        return new StringValue(parser.getTenant().getTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        SqlParser parser = SqlParserContext.getParser();
        return parser.getTenant().getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // true 不解析， false 解析
        MybatisProperties.TableMode golbaltableMode = SqlParserHelper.getTenantTablesModeCache(SqlParserHelper.GOLBAL_TENANT_TABLES_MODE_CACHE);
        if(MybatisProperties.TableMode.EXCLUDE.equals(golbaltableMode)) {
            return SqlParserHelper.getTenantTablesModeCache(tableName) != null;
        } else {
            return SqlParserHelper.getTenantTablesModeCache(tableName) == null;
        }
    }
}
