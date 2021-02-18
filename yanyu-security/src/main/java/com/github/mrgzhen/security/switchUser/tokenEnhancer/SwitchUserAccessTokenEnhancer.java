package com.github.mrgzhen.security.switchUser.tokenEnhancer;

import com.github.mrgzhen.core.constant.AppConstant;
import com.github.mrgzhen.security.switchUser.resolver.SwitchUserAuthenticationResolver;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanyu
 */
public class SwitchUserAccessTokenEnhancer {

    private SwitchUserAuthenticationResolver resolver = new SwitchUserAuthenticationResolver();
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken) {
        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
        info.put(AppConstant.SWITCH_USER_ACCESS_TOKEN, "switch");
        DefaultOAuth2AccessToken result = (DefaultOAuth2AccessToken)accessToken;
        result.setAdditionalInformation(info);
        return result;
    }
    public OAuth2AccessToken inhance(OAuth2AccessToken accessToken) {
        Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
        info.remove(AppConstant.SWITCH_USER_ACCESS_TOKEN);
        DefaultOAuth2AccessToken result = (DefaultOAuth2AccessToken)accessToken;
        result.setAdditionalInformation(info);
        return result;
    }
}
