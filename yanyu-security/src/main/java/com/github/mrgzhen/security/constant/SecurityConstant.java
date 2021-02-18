package com.github.mrgzhen.security.constant;

/**
 * @author yanyu
 */
public interface SecurityConstant {

    /** 模式 */
    String[] GRANT_TYPES = new String[] {
            /** 授权码模式 */
            "authorization_code",
            /** 密码模式 */
            "password",
            /** 客户端模式 */
            "client_credentials",
            /** 隐模式*/
            "implicit",
            /** 刷新 */
            "refresh_token",
            /** 手机验证码 */
            "sms"
    };

    /** 忽略的url路径 */
    String[] IGNORING_URL = new String[]{"/error", "/actuator/**"};

    /** 切换用户url */
    String SWITCHUSER_LOGIN_URL = "/auth/login/impersonate";
    /** 退出用户切换url */
    String SWITCHUSER_LOGOUT_URL = "/auth/logout/impersonate";

    /** 手机验证码登录uri */
    String AUTH_SMS_LOGIN_URL = "/auth/login/sms";
    /** 第三方登陆 */
    String AUTH_SOCIAL_LOGIN_URL = "/auth/login/social";
    /** 密码账号登陆 */
    String AUTH_LOGIN_URL = "/auth/login";
    /** 获取认证信 */
    String AUTH_ME_URL = "/auth/me";
    /** 验证码生成 */
    String IMAGE_CODE_URL = "/auth/code/image";
    /** 手机验证码生成 */
    String SMS_CODE_URL = "/auth/code/sms";
    /** 密码账号退出 */
    String AUTH_LOGOUT_URL = "/auth/logout";


    String CLIENT_ID = "yanyu";
    String CLIENT_SCOPE = "all";
    String CLIENT_SECRET = "$2a$10$wh6S2EX9i5.lbH1JNeTIje/9O4bgL916YCFcqZhFJhRoBGFBHkXsW";//

    /** 当前url 规则 */
    String REQUESTURL_SQL_PARSER_RULE_KEY = "REQUESTURL_SQL_PARSER_RULE";
    /** 是否被认证过 */
    String AUTHENTICATED_SQL_PARSER_RULE_KEY = "AUTHENTICATED_SQL_PARSER_RULE";
    /** 认证信息 */
    String AUTHENTICATION_SQL_PARSER_RULE_KEY = "AUTHENTICATION_SQL_PARSER_RULE";
}
