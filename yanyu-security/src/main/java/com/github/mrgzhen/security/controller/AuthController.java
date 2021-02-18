package com.github.mrgzhen.security.controller;

import com.github.mrgzhen.core.web.Result;
import com.github.mrgzhen.security.tokenService.CustomUserInfoTokenServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanyu
 */
@RestController
public class AuthController {

    @Autowired
    private ConsumerTokenServices tokenServices;
    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    @GetMapping(value = "${yanyu.security.auth.auth-me:/auth/me}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "获取用户信息")
    public Authentication authMe() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping(value = "${yanyu.security.auth.logout-uri:/auth/logout}", produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "退出")
    public Result logout(HttpServletRequest request) {
        Authentication authentication = tokenExtractor.extract(request);
        tokenServices.revokeToken(String.valueOf(authentication.getPrincipal()));
        return Result.success();
    }


}
