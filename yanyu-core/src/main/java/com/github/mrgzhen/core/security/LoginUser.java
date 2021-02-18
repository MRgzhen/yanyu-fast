package com.github.mrgzhen.core.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    /** 当前登录用户唯一标识*/
    private String uuid;

    /** 用户id */
    private String userId;

    /** 用户归属部门id */
    private String deptId;

    /** 租户id */
    private String tenantId;

    /** 用户名 */
    private String username;

    /** 用户密码 */
    private String password;

    /** 是否是默认的系统用户 */
    private Boolean isSys;

    /** 是否是运营租户 */
    private Boolean isPlat;

    /** 角色集 */
    private Set<String> roles;

    /** 权限集 */
    private Set<String> perms;

    public LoginUser(String uuid, String username, String password) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
    }
}
