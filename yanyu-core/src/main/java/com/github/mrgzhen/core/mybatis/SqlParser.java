package com.github.mrgzhen.core.mybatis;

import lombok.*;

import java.util.Set;

/**
 * @author yanyu
 */
@NoArgsConstructor
@Getter
public class SqlParser {

    /** 租户 */
    private Tenant tenant;
    /** 数据权限 */
    private DataScope dataScope;
    /** 自动注入 */
    private AutoFill autoFill;

    public SqlParser tenant(String tenantIdColumn, String tenantId) {
        this.tenant = new Tenant(tenantIdColumn, tenantId);
        return this;
    }

    public SqlParser dataScope(String dataScopeColumn, Set<String> dataScopes, Boolean enabled) {
        this.dataScope = new DataScope(dataScopeColumn, dataScopes, enabled);
        return this;
    }

    public SqlParser dataScope(String dataScopeColumn, Set<String> dataScopes) {
        this.dataScope = new DataScope(dataScopeColumn, dataScopes, true);
        return this;
    }

    public SqlParser autoFill(String userId, String deptId) {
        this.autoFill = new AutoFill(userId, deptId);
        return this;
    }

    public void hold() {
        SqlParserContext.setParser(this);
    }

    @Data
    @AllArgsConstructor
    public class DataScope {
        /** 数据权限数据库列名*/
        private final String dataScopeColumn;

        /** 数据权限 */
        private final Set<String> dataScopes;

        /** 是否解析, true 不解析， false解析 */
        private final Boolean enabled;
    }

    @Data
    @AllArgsConstructor
    public class Tenant {
        /** 租户数据类名*/
        private final String tenantIdColumn;

        /** 租户值 */
        private final String tenantId;
    }

    @Data
    @AllArgsConstructor
    public class AutoFill {
        /** 创建人 */
        private final String userId;

        /** 部门 */
        private final String deptId;
    }
}
