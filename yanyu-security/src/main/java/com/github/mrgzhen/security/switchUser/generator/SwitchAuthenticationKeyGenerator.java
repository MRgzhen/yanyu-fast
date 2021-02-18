package com.github.mrgzhen.security.switchUser.generator;

import com.github.mrgzhen.core.security.LoginUser;
import com.github.mrgzhen.core.security.LoginUserContext;
import com.github.mrgzhen.security.switchUser.resolver.SwitchUserAuthenticationResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 用户切换token生成规则
 * @author yanyu
 */
public class SwitchAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

    private static final String CLIENT_ID = "client_id";
    private static final String SCOPE = "scope";
    private static final String USERNAME = "username";
    private static final String UUID = "UUID";
    private static final String TENANT = "TENANT";

    private SwitchUserAuthenticationResolver resolver = new SwitchUserAuthenticationResolver();

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        if (!authentication.isClientOnly()) {
            LoginUser loginUser = null;
            Authentication switchUserOriginAuth = resolver.getSourceAuthentication(authentication);
            if(switchUserOriginAuth != null) {
                loginUser = LoginUserContext.getLoginUserThrow(switchUserOriginAuth);
                values.put(USERNAME, switchUserOriginAuth.getName());
            } else {
                loginUser = LoginUserContext.getLoginUserThrow(authentication);
                values.put(USERNAME, authentication.getName());
            }

            if(loginUser != null) {
                values.put(UUID, loginUser.getUuid());
                values.put(TENANT, loginUser.getTenantId());
                values.put(USERNAME, loginUser.getUsername());
            }
        }

        OAuth2Request authorizationRequest = authentication.getOAuth2Request();
        values.put(CLIENT_ID, authorizationRequest.getClientId());
        if (authorizationRequest.getScope() != null) {
            values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
        }
        return super.generateKey(values);
    }

}
