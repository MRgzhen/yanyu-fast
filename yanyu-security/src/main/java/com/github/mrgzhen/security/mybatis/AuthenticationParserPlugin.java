package com.github.mrgzhen.security.mybatis;

import com.github.mrgzhen.core.mybatis.SqlParser;
import com.github.mrgzhen.core.mybatis.SqlParserContext;
import com.github.mrgzhen.core.mybatis.SqlParserPlugin;
import com.github.mrgzhen.core.mybatis.SqlParserRule;
import com.github.mrgzhen.core.security.LoginUser;
import com.github.mrgzhen.core.security.LoginUserContext;
import com.github.mrgzhen.security.constant.SecurityConstant;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashSet;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationParserPlugin implements SqlParserPlugin {

    @Override
    public SqlParser build(SqlParserRule rule) {
        LoginUser loginUser = (LoginUser) rule.get(SecurityConstant.AUTHENTICATION_SQL_PARSER_RULE_KEY);
        SqlParser parser = new SqlParser();
        return SqlParserContext.builder().tenant("tenant_id",loginUser.getTenantId())
                .dataScope("ds_dept",new HashSet<String>(Arrays.asList(loginUser.getDeptId())), loginUser.getIsSys())
                .autoFill(loginUser.getUserId(),loginUser.getDeptId());
    }

    @Override
    public boolean supports(SqlParserRule delimiter) {
        Boolean authenticated = (Boolean) delimiter.get(SecurityConstant.AUTHENTICATED_SQL_PARSER_RULE_KEY);
        return authenticated;
    }
}
