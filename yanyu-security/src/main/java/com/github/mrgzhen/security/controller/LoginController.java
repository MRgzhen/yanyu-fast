package com.github.mrgzhen.security.controller;

import com.github.mrgzhen.core.web.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;

/**
 * @author yanyu
 */
@FrameworkEndpoint
@Api(tags="登录")
public class LoginController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping("${yanyu.security.auth.login-uri:/auth/login}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name= "password" ,value = "密码",dataType = "string"),
            @ApiImplicitParam(paramType = "query",name= "username" ,value = "用户名",dataType = "string")
    })
    @ApiOperation(value = "密码登录")
    @ResponseBody
    public Result login(@ApiIgnore Principal principal,
                        @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> result = tokenEndpoint.postAccessToken(principal, parameters);
        return Result.success(result.getBody());
    }

    @PostMapping("${yanyu.security.auth.sms-login-uri:/auth/login/sms}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name= "phone" ,value = "手机",dataType = "string")
    })
    @ApiOperation(value = "手机验证码登录")
    @ResponseBody
    public Result smsLogin(@ApiIgnore Principal principal,
                        @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> result = tokenEndpoint.postAccessToken(principal, parameters);
        return Result.success(result.getBody());
    }

    @PostMapping("${yanyu.security.auth.social-login-uri:/auth/login/social}")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name= "openId" ,value = "唯一标识",dataType = "string"),
            @ApiImplicitParam(paramType = "query",name= "app" ,value = "第三方登录标识",dataType = "string")
    })
    @ApiOperation(value = "第三方登录登录")
    @ResponseBody
    public Result socialLogin(@ApiIgnore Principal principal,
                        @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> result = tokenEndpoint.postAccessToken(principal, parameters);
        return Result.success(result.getBody());
    }
}
