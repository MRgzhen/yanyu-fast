package com.github.mrgzhen.security.tokenGranter.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yanyu
 */
public interface SocialUserDetailService {

    UserDetails loadUserByUsername(String openId,String app) throws UsernameNotFoundException;
}
