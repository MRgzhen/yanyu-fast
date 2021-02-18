package com.github.mrgzhen.security.switchUser.resolver;

import com.github.mrgzhen.core.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

import java.util.Collection;

/**
 * @author yanyu
 */
@Slf4j
public class SwitchUserAuthenticationResolver {

    public boolean isSwtichUser(Authentication current) {
        Authentication original = getSourceAuthentication(current);
        if (original == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取原始authentication
     */
    public Authentication getSourceAuthentication(Authentication current) {
        if (current == null || !current.isAuthenticated()) {
            throw new AuthenticationException();
        }

        Authentication original = null;
        Collection<? extends GrantedAuthority> authorities = current.getAuthorities();

        for (GrantedAuthority auth : authorities) {
            if (auth instanceof SwitchUserGrantedAuthority) {
                original = ((SwitchUserGrantedAuthority) auth).getSource();
            }
        }
        return original;
    }
}
