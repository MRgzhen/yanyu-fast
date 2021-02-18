package com.github.mrgzhen.security.principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrgzhen.core.security.LoginUser;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录用户信息
 * @author yanyu
 */
@ToString(callSuper = true)
public class AuthUser extends LoginUser implements UserDetails {

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public AuthUser(String uuid, String username, String password, boolean accountNonExpired,
                    boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        super(uuid, username, password);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public AuthUser() {
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authoritys = new HashSet<>();

        if(CollectionUtils.isNotEmpty(super.getPerms())) {
            super.getPerms().parallelStream().forEach(perm -> {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(perm);
                authoritys.add(authority);
            });
        }

        if(CollectionUtils.isNotEmpty(super.getRoles())) {
            super.getRoles().parallelStream().forEach(role -> {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(StringUtils.join("ROLE_", role));
                authoritys.add(authority);
            });
        }
        return authoritys;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
