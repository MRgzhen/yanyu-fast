package com.github.mrgzhen.security.switchUser.tokenEnhancer.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yanyu
 */
public interface SwitchUserDetailsService {

    UserDetails loadUserByUsername(String username, String tenantCode);
}
