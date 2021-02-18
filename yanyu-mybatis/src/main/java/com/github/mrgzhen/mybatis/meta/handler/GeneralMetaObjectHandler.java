package com.github.mrgzhen.mybatis.meta.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.mrgzhen.core.mybatis.SqlParserContext;
import com.github.mrgzhen.core.util.StringUtil;
import com.github.mrgzhen.mybatis.MybatisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author yanyu
 */
@Slf4j
public class GeneralMetaObjectHandler implements MetaObjectHandler {

    private MybatisProperties mybatisProperties;
    public String CREATEUSER_NAMEINBEAN;
    public String CREATETIME_NAMEINBEAN;
    public String UPDATEUSER_NAMEINBEAN;
    public String UPDATETIME_NAMEINBEAN;
    public String DSDEPT_NAMEINBEAN;

    public GeneralMetaObjectHandler(MybatisProperties mybatisProperties) {
        this.mybatisProperties = mybatisProperties;
        this.CREATEUSER_NAMEINBEAN = StringUtil.lineToCamel(this.mybatisProperties.getAutoFill().getCreateUserNameInTable());
        this.CREATETIME_NAMEINBEAN = StringUtil.lineToCamel(this.mybatisProperties.getAutoFill().getCreateTimeNameInTable());
        this.UPDATEUSER_NAMEINBEAN = StringUtil.lineToCamel(this.mybatisProperties.getAutoFill().getUpdateUserNameInTable());
        this.UPDATETIME_NAMEINBEAN = StringUtil.lineToCamel(this.mybatisProperties.getAutoFill().getUpdateTimeNameInTable());
        this.DSDEPT_NAMEINBEAN = StringUtil.lineToCamel(this.mybatisProperties.getAutoFill().getDsDeptNameInTable());
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        boolean createUser = metaObject.hasSetter(CREATEUSER_NAMEINBEAN);
        if (createUser) {
            this.strictInsertFill(metaObject, CREATEUSER_NAMEINBEAN, String.class, SqlParserContext.getParser().getAutoFill().getUserId());
        }
        boolean createTime = metaObject.hasSetter(CREATETIME_NAMEINBEAN);
        if (createTime) {
            this.strictInsertFill(metaObject, CREATETIME_NAMEINBEAN, Date.class, new Date());
        }
        boolean dsDept = metaObject.hasSetter(DSDEPT_NAMEINBEAN);
        String deptId = SqlParserContext.getParser().getAutoFill().getDeptId();
        if (dsDept && StringUtils.isNoneBlank(deptId)) {
            this.strictInsertFill(metaObject, DSDEPT_NAMEINBEAN, String.class, SqlParserContext.getParser().getAutoFill().getDeptId());
        }
        boolean updateUser = metaObject.hasSetter(UPDATEUSER_NAMEINBEAN);
        if (updateUser) {
            this.strictInsertFill(metaObject, UPDATEUSER_NAMEINBEAN, String.class, SqlParserContext.getParser().getAutoFill().getUserId());
        }
        boolean updateTime = metaObject.hasSetter(UPDATETIME_NAMEINBEAN);
        if (updateTime) {
            this.strictInsertFill(metaObject, UPDATETIME_NAMEINBEAN,  Date.class, new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean updateUser = metaObject.hasSetter(UPDATEUSER_NAMEINBEAN);
        if (updateUser) {
            this.strictInsertFill(metaObject, UPDATEUSER_NAMEINBEAN, String.class, SqlParserContext.getParser().getAutoFill().getUserId());
        }
        boolean updateTime = metaObject.hasSetter(UPDATETIME_NAMEINBEAN);
        if (updateTime) {
            this.strictInsertFill(metaObject, UPDATETIME_NAMEINBEAN,  Date.class, new Date());
        }
    }
}
