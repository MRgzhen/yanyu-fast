package com.github.mrgzhen.security.switchUser.matcher;

import com.github.mrgzhen.security.SecurityProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanyu
 */
public class SwitchUserRequestMatcher {

    private SecurityProperties properties;
    private RequestMatcher exitUserMatcher;
    private RequestMatcher switchUserMatcher;

    public SwitchUserRequestMatcher(SecurityProperties properties) {
        this.properties = properties;
        init();
    }

    public void init() {
        exitUserMatcher = createMatcher(properties.getSwitchUser().getLogoutUri());
        switchUserMatcher = createMatcher(properties.getSwitchUser().getLoginUri());
    }

    private RequestMatcher createMatcher(String pattern) {
        return new AntPathRequestMatcher(pattern, "POST", true, new UrlPathHelper());
    }

    public boolean requiresExitUser(HttpServletRequest request) {
        return this.exitUserMatcher.matches(request);
    }

    public boolean requiresSwitchUser(HttpServletRequest request) {
        return this.switchUserMatcher.matches(request);
    }
}
