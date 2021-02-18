package com.github.mrgzhen.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.mrgzhen.core.util.AssertUtil;
import com.github.mrgzhen.mybatis.datascope.DsLineInnerInterceptor;
import com.github.mrgzhen.mybatis.datascope.GeneralDsHandler;
import com.github.mrgzhen.mybatis.meta.handler.GeneralMetaObjectHandler;
import com.github.mrgzhen.mybatis.parser.SqlParserHelper;
import com.github.mrgzhen.mybatis.parser.SqlParserProxy;
import com.github.mrgzhen.mybatis.tenant.GeneralTenantHandler;
import com.github.mrgzhen.mybatis.tenant.GeneralTenantInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author yanyu
 */
@Configuration
@EnableConfigurationProperties(value={MybatisProperties.class})
public class MybatisConfiguration {
    @Bean
    public SqlParserProxy sqlParserProxy() {
        return new SqlParserProxy();
    }

    @Bean
    public GeneralMetaObjectHandler generalMetaObjectHandler(MybatisProperties mybatisProperties) {
        return new GeneralMetaObjectHandler(mybatisProperties);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisProperties properties) {
        // 初始化sql解析帮助类
        ConvertPropOfToMemoryThread thread = new ConvertPropOfToMemoryThread(properties);
        thread.setDaemon(true);
        thread.start();

        // 定义mybatisPlus拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInterceptor);

        // 租户解析器
        if(properties.getTenant().getEnabled()) {
            GeneralTenantInnerInterceptor tenantInterceptor = new GeneralTenantInnerInterceptor();
            tenantInterceptor.setGeneralTenantHandler(new GeneralTenantHandler());
            interceptor.addInnerInterceptor(tenantInterceptor);
        }

        // 数据权限解析器
        if(properties.getDataScope().getEnabled()) {
            DsLineInnerInterceptor dsInterceptor = new DsLineInnerInterceptor();
            dsInterceptor.setDsHandler(new GeneralDsHandler());
            interceptor.addInnerInterceptor(dsInterceptor);
        }
        return interceptor;
    }


    @Slf4j
    static class ConvertPropOfToMemoryThread extends Thread{

        private static final String threadName = "Convert-PropTableModelToMemoryThread";
        private MybatisProperties properties;
        public ConvertPropOfToMemoryThread(MybatisProperties properties) {
            super(threadName);
            AssertUtil.isNotNull(properties, "mybatis配置文件错误");
            this.properties = properties;
        }

        @Override
        public void run() {
            if(properties.getTenant().getEnabled()) {
                SqlParserHelper.setTenantTablesModeCache(SqlParserHelper.GOLBAL_TENANT_TABLES_MODE_CACHE, properties.getTenant().getMode());
                Optional.ofNullable(properties.getTenant().getTables()).ifPresent(tables -> tables.forEach(table -> {
                    SqlParserHelper.setTenantTablesModeCache(table, properties.getTenant().getMode());
                }));
                log.info("租户sql解析模式为: 【{}】, 相关表：【{}】", properties.getTenant().getMode(),
                        StringUtils.join(SqlParserHelper.getTenantTablesModeCacheMap().keySet(), ","));
            } else {
                log.info("租户sql解析被禁用");
            }


            if(properties.getDataScope().getEnabled()) {
                SqlParserHelper.setDsTablesModeCache(SqlParserHelper.GOLBAL_DS_TABLES_MODE_CACHE, properties.getDataScope().getMode());
                Optional.ofNullable(properties.getDataScope().getTables()).ifPresent(tables -> tables.forEach(table -> {
                    SqlParserHelper.setDsTablesModeCache(table, properties.getDataScope().getMode());
                }));
                log.info("数据权限sql解析模式为: 【{}】, 相关表：【{}】", properties.getDataScope().getMode(),
                        StringUtils.join(SqlParserHelper.getDSTablesModeCacheMap().keySet(), ","));
            } else {
                log.info("数据权限sql解析被禁用");
            }

        }
    }
}
