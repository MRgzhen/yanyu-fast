package com.github.mrgzhen.mybatis.datascope;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.github.mrgzhen.core.mybatis.SqlParser;
import com.github.mrgzhen.core.mybatis.SqlParserContext;
import com.github.mrgzhen.mybatis.MybatisProperties;
import com.github.mrgzhen.mybatis.parser.SqlParserHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 排除数据库表，不在该表后拼接租户标识
 * @author yanyu
 */
@Slf4j
public class GeneralDsHandler {

    public boolean ignoreMethod() {
        return !SqlParserHelper.getSqlParserAttributeHolder().isDs();
    }

    public boolean isAllDataScope() {
        SqlParser parser = SqlParserContext.getParser();
        return parser.getDataScope().getEnabled();
    }

    public Expression getDsDeptId() {
        SqlParser parser = SqlParserContext.getParser();
        ValueListExpression expression = new ValueListExpression();
        Set<String> dataScopes = parser.getDataScope().getDataScopes();
        List<Expression> expressions = new ArrayList<>();
        for(String dataScope : dataScopes) {
            expressions.add(new StringValue(dataScope));
        }
        expression.setExpressionList(new ExpressionList(expressions));
        return expression;
    }

    public String getdsDeptColumn() {
        SqlParser parser = SqlParserContext.getParser();
        String dataScopeColumn = parser.getDataScope().getDataScopeColumn();
        return dataScopeColumn;
    }

    public boolean ignoreTable(String tableName) {
        // true 不解析， false 解析
        MybatisProperties.TableMode golbaltableMode = SqlParserHelper.getDSTablesModeCache(SqlParserHelper.GOLBAL_TENANT_TABLES_MODE_CACHE);
        if(MybatisProperties.TableMode.EXCLUDE.equals(golbaltableMode)) {
            return SqlParserHelper.getDSTablesModeCache(tableName) != null;
        } else {
            return SqlParserHelper.getDSTablesModeCache(tableName) == null;
        }
    }
}
