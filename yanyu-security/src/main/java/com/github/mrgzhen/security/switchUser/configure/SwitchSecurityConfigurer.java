package com.github.mrgzhen.security.switchUser.configure;

import com.github.mrgzhen.core.exception.handler.GeneralErrorAttributesResolver;
import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.switchUser.filter.SwitchFormContentFilter;
import com.github.mrgzhen.security.switchUser.filter.SwitchUserFilter;
import com.github.mrgzhen.security.switchUser.handler.SwitchUserAuthenticationFailureHandler;
import com.github.mrgzhen.security.switchUser.handler.SwitchUserAuthenticationSuccessHandler;
import com.github.mrgzhen.security.switchUser.matcher.SwitchUserRequestMatcher;
import com.github.mrgzhen.security.switchUser.tokenEnhancer.service.SwitchUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author yanyu
 */
@AllArgsConstructor
public class SwitchSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthorizationServerTokenServices tokenServices;
    private SecurityProperties properties;
    private SwitchUserDetailsService userDetailsService;
    private GeneralErrorAttributesResolver errorAttributesResolver;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        SwitchUserRequestMatcher matcher = new SwitchUserRequestMatcher(properties);
        SwitchUserFilter switchUserFilter = new SwitchUserFilter(matcher, userDetailsService);
        switchUserFilter.setFailureHandler(new SwitchUserAuthenticationFailureHandler(matcher,errorAttributesResolver));
        switchUserFilter.setSuccessHandler(new SwitchUserAuthenticationSuccessHandler(tokenServices,matcher,errorAttributesResolver));
        builder.addFilterAfter(switchUserFilter, FilterSecurityInterceptor.class);

        SwitchFormContentFilter skySwitchFormContentFilter = new SwitchFormContentFilter(matcher);
        builder.addFilterBefore(skySwitchFormContentFilter, SwitchUserFilter.class);
    }
}