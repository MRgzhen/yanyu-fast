package com.github.mrgzhen.core.security;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * 登录用户上下文
 * @author yanyu
 */
@Slf4j
public class LoginUserContext {

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return parseLoginUser(authentication);
    }

    public static LoginUser getLoginUserThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getLoginUserThrow(authentication);
    }


    public static LoginUser getLoginUserThrow(Authentication authentication) {
        LoginUser loginUser = parseLoginUser(authentication);
        if(loginUser == null) {
            throw new AuthenticationException();
        } else {
            return loginUser;
        }
    }

    private static LoginUser parseLoginUser(Authentication authentication) {
       if(!authentication.isAuthenticated()) {
            return null;
        } else {
            LoginUser loginUser = null;
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if(userDetails instanceof LoginUser) {
                    loginUser = new LoginUser();
                    BeanUtils.copyProperties(userDetails, loginUser);
                    loginUser.setPassword(null);
                }
            } else {
                try {
                    loginUser = JSONUtil.instant().readValue(
                            JSONUtil.instant().writeValueAsString(principal), LoginUser.class);
                    loginUser.setPassword(null);
                } catch(Exception e) {
                    log.warn("解析LoginUser错误，{}",e.getMessage());
                }
            }
            return loginUser;
        }
    }
}
