package com.github.mrgzhen.mybatis.parser;


import com.github.mrgzhen.mybatis.MybatisProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yanyu
 */
@Slf4j
public class SqlParserHelper {

    /**
     * 解析SkySqlParser注解属性
     */
    private static final ThreadLocal<Deque<SqlParserAttribute>> SQL_PARSER_ATTRIBUTE_HOLDER = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new ArrayDeque<>();
        }
    };

    /**
     * 全局租户模式GOLBAL_TENANT_TABLES_MODE_CACHE中key值
     */
    public static final String GOLBAL_TENANT_TABLES_MODE_CACHE = "GOLBAL_TENANT_TABLES_MODE";
    /**
     * 配置文件中租户表解析模式
     * */
    private static final Map<String, MybatisProperties.TableMode> TENANT_TABLES_MODE_CACHE = new ConcurrentHashMap<>();

    /**
     * 全局数据权限模式DS_TABLES_MODE_CACHE中key值
     */
    public static final String GOLBAL_DS_TABLES_MODE_CACHE = "GOLBAL_DS_TABLES_MODE";

    /**
     * 配置文件中数据权限表解析模式
     * */
    private static final Map<String, MybatisProperties.TableMode> DS_TABLES_MODE_CACHE = new ConcurrentHashMap<>();

    static void setSqlParserAttributeHolder(SqlParser skySqlParser, String methodName) {
        SqlParserAttribute beforeAttribute = getSqlParserAttributeHolder();
        SqlParserAttribute currentAttribute = SqlParserAttribute.build(skySqlParser, methodName);
        currentAttribute.setTenant(beforeAttribute.isTenant() && currentAttribute.isTenant());
        currentAttribute.setDs(beforeAttribute.isDs() && currentAttribute.isDs());
        SQL_PARSER_ATTRIBUTE_HOLDER.get().push(currentAttribute);
        log.debug("方法：【{}】入栈， 租户解析标识：{}，数据权限解析标识：{}，当前队列长度:{}",
                currentAttribute.getMethodName(), currentAttribute.isDs(), currentAttribute.isTenant(), SQL_PARSER_ATTRIBUTE_HOLDER.get().size());
    }

    static void removeSqlParserAttributeHolder() {
        Deque<SqlParserAttribute> attributeDeque = SQL_PARSER_ATTRIBUTE_HOLDER.get();
        try {
            SqlParserAttribute attribute = attributeDeque.pop();
            log.debug("方法：【{}】出栈， 租户解析标识：{}，数据权限解析标识：{}，当前队列长度:{}",
                    attribute.getMethodName(), attribute.isDs(), attribute.isTenant(), SQL_PARSER_ATTRIBUTE_HOLDER.get().size());

        } catch (Exception e) {
            log.warn("出栈异常：{}",e.getMessage());
        }
        if(CollectionUtils.isEmpty(attributeDeque)) {
            SQL_PARSER_ATTRIBUTE_HOLDER.remove();
        }
    }

    public static SqlParserAttribute getSqlParserAttributeHolder() {
        try {
            return SQL_PARSER_ATTRIBUTE_HOLDER.get().getFirst();
        } catch (Exception e) {
            return SqlParserAttribute.init();
        }
    }

    public static void setTenantTablesModeCache(String tableName, MybatisProperties.TableMode tableMode) {
        TENANT_TABLES_MODE_CACHE.putIfAbsent(tableName, tableMode);
    }
    public static MybatisProperties.TableMode getTenantTablesModeCache(String tableName) {
        return TENANT_TABLES_MODE_CACHE.get(tableName);
    }
    public static Map<String, MybatisProperties.TableMode> getTenantTablesModeCacheMap() {
        return MapUtils.unmodifiableMap(TENANT_TABLES_MODE_CACHE);
    }
    public static void setDsTablesModeCache(String tableName, MybatisProperties.TableMode tableMode) {
        DS_TABLES_MODE_CACHE.putIfAbsent(tableName, tableMode);
    }
    public static MybatisProperties.TableMode getDSTablesModeCache(String tableName) {
        return DS_TABLES_MODE_CACHE.get(tableName);
    }
    public static Map<String, MybatisProperties.TableMode> getDSTablesModeCacheMap() {
        return MapUtils.unmodifiableMap(DS_TABLES_MODE_CACHE);
    }
    @Data
    public static class SqlParserAttribute {

        private String methodName;

        private boolean ds;

        private boolean tenant;

        public static SqlParserAttribute init() {
            SqlParserAttribute attribute = new SqlParserAttribute();
            attribute.setDs(true);
            attribute.setTenant(true);
            attribute.setMethodName("");
            return attribute;
        }

        public static SqlParserAttribute build(SqlParser skySqlParser, String methodName) {
            SqlParserAttribute attribute = new SqlParserAttribute();
            attribute.setDs(skySqlParser.ds());
            attribute.setTenant(skySqlParser.tenant());
            attribute.setMethodName(methodName);
            return attribute;
        }
    }
}
