package com.github.mrgzhen.security;

import com.github.mrgzhen.core.constant.AppConstant;
import com.github.mrgzhen.security.constant.SecurityConstant;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * 认证授权忽略路径
 * @author yanyu
 */
@Data
@ConfigurationProperties(prefix = "yanyu.security")
public class SecurityProperties {

    /** 认证过和匿名都允许的url */
    private String[] permits;

    /** 绕过spring security的所有filter */
    private String[] ignores;

    /** 用户切换 */
    private SwitchUser switchUser = new SwitchUser();

    /** 认证服务器配置 */
    private Auth auth = new Auth();

    @PostConstruct
    public void init() {

        if(this.ignores == null || this.ignores.length == 0) {
            this.ignores = new String[]{};
        }
        this.ignores = ArrayUtils.addAll(this.ignores, SecurityConstant.IGNORING_URL);

        if(this.permits == null || this.permits.length == 0) {
            this.permits = new String[]{};
        }
        this.permits = ArrayUtils.addAll(this.permits, StringUtils.join(AppConstant.CLIENT_INTERNAL_CALL,"/**"));
    }

    @Data
    public class SwitchUser {

        /** 是否启用 */
        private Boolean enabled;

        /** 切换用户url */
        private String loginUri = SecurityConstant.SWITCHUSER_LOGIN_URL;

        /** 退出用户切换url */
        private String logoutUri = SecurityConstant.SWITCHUSER_LOGOUT_URL;
    }

    @Data
    public class Auth {

        /** 手机登陆登录uri */
        private String smsLoginUri = SecurityConstant.AUTH_SMS_LOGIN_URL;

        /** 第三方登陆url */
        private String socialLoginUri = SecurityConstant.AUTH_SOCIAL_LOGIN_URL;

        /** 密码账号登陆url */
        private String loginUri = SecurityConstant.AUTH_LOGIN_URL;

        /** 图形验证码地址 */
        private String imageCodeUri = SecurityConstant.IMAGE_CODE_URL;

        /** 手机验证码地址 */
        private String smsCodeUri = SecurityConstant.SMS_CODE_URL;

        /** 图形验证码是否启用 */
        private boolean imageCodeEnabled = false;

        /** 手机验证码是否开启 */
        private boolean smsCodeEnabled = true;

        /** 退出url */
        private String logoutUri = SecurityConstant.AUTH_LOGOUT_URL;

        /** 获取认证信息 */
        private String authMeUri = SecurityConstant.AUTH_ME_URL;
    }
}
