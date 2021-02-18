package com.github.mrgzhen.mybatis;

import com.baomidou.mybatisplus.annotation.SqlParser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author yanyu
 */
@Data
@ConfigurationProperties(prefix = "yanyu.mybatis")
public class MybatisProperties {

    /** 租户配置 */
    private Tenant tenant = new Tenant();

    /** 数据权限配置 */
    private DataScope dataScope = new DataScope();

    /** 自动填充 */
    private AutoFill autoFill = new AutoFill();

    @Data
    public class Tenant {

        /** 是否启用租户解析 */
        private Boolean enabled = Boolean.FALSE;

        /** 租户采用模式 */
        private TableMode mode = TableMode.EXCLUDE;

        /** mode等于INCLUDE，租户只针对tables属性中的表，mode等于EXCLUDE，租户针对不在tables属性中的表 */
        private List<String> tables;
    }

    @Data
    public class DataScope {

        /** 是否启用数据权限解析 */
        private Boolean enabled = Boolean.FALSE;

        /** 数据权限采用模式 */
        private TableMode mode = TableMode.EXCLUDE;

        /** mode等于INCLUDE，数据权限只针对tables属性中的表，mode等于EXCLUDE，数据权限针对不在tables属性中的表 */
        private List<String> tables;
    }

    @Data
    public class AutoFill {

        /** 创建人在表中的名字 */
        private String createUserNameInTable="create_user";

        /** 创建时间在表中的名字 */
        private String createTimeNameInTable="create_time";

        /** 更新人在表中的名字 */
        private String updateUserNameInTable="update_user";

        /** 更新时间在表中的名字 */
        private String updateTimeNameInTable="update_time";

        /** 数据权限 */
        private String dsDeptNameInTable = "ds_dept";
    }

    public enum TableMode {
        /**
         * 包含的表
         */
        INCLUDE,
        /**
         * 排除的表
         */
        EXCLUDE
    }
}
