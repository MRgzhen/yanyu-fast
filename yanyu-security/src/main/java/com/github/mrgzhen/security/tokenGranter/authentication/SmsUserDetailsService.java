package com.github.mrgzhen.security.tokenGranter.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yanyu
 */
public interface SmsUserDetailsService {

    UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException;
}
