package com.github.mrgzhen.security.generator;

import com.github.mrgzhen.core.security.LoginUser;
import com.github.mrgzhen.core.security.LoginUserContext;
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
public class CustomAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

    private static final String CLIENT_ID = "client_id";

    private static final String SCOPE = "scope";

    private static final String USERNAME = "username";

    private static final String UUID = "UUID";

    private static final String TENANT = "TENANT";

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        if (!authentication.isClientOnly()) {
            LoginUser loginUser = LoginUserContext.getLoginUserThrow(authentication);
            if(loginUser != null) {
                values.put(UUID, loginUser.getUuid());
                values.put(TENANT, loginUser.getTenantId());
                values.put(USERNAME, loginUser.getUsername());
            } else {
                values.put(USERNAME, authentication.getName());
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
