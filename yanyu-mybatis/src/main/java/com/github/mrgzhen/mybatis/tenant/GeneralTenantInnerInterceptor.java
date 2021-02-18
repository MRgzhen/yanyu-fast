package com.github.mrgzhen.mybatis.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yanyu
 */
public class GeneralTenantInnerInterceptor extends TenantLineInnerInterceptor {
    private GeneralTenantHandler generalTenantHandler;

    public GeneralTenantInnerInterceptor() {
        super();
    }

    public void setGeneralTenantHandler(GeneralTenantHandler generalTenantHandler) {
        super.setTenantLineHandler(generalTenantHandler);
        this.generalTenantHandler = generalTenantHandler;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (generalTenantHandler.ignoreMethod()) return;
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        if (generalTenantHandler.ignoreMethod()) return;
        super.beforePrepare(sh, connection, transactionTimeout);
    }
}
