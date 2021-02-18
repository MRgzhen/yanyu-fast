package com.github.mrgzhen.security.switchUser.filter;

import com.github.mrgzhen.core.constant.AppConstant;
import com.github.mrgzhen.security.switchUser.matcher.SwitchUserRequestMatcher;
import com.github.mrgzhen.security.switchUser.resolver.SwitchUserAuthenticationResolver;
import com.github.mrgzhen.security.switchUser.tokenEnhancer.service.SwitchUserDetailsService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.security.web.authentication.switchuser.SwitchUserAuthorityChanger;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 切换用户
 * @author yanyu
 */
public class SwitchUserFilter extends GenericFilterBean
        implements ApplicationEventPublisherAware, MessageSourceAware {

    public static final String SPRING_SECURITY_SWITCH_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_SWITCH_TENANT_KEY = "tenantCode";
    private String usernameParameter = SPRING_SECURITY_SWITCH_USERNAME_KEY;
    private String tenantCodeParameter = SPRING_SECURITY_SWITCH_TENANT_KEY;
    private String switchAuthorityRole = AppConstant.SWITCH_ROLE_ADMINISTRATOR;
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private SwitchUserAuthenticationResolver resolver = new SwitchUserAuthenticationResolver();
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private SwitchUserRequestMatcher switchUserRequestMatcher;
    private SwitchUserAuthorityChanger switchUserAuthorityChanger;

    private ApplicationEventPublisher eventPublisher;
    private SwitchUserDetailsService userDetailsService;


    public SwitchUserFilter(SwitchUserRequestMatcher switchUserRequestMatcher, SwitchUserDetailsService userDetailsService) {
        this.switchUserRequestMatcher = switchUserRequestMatcher;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // check for switch or exit request
        if (this.switchUserRequestMatcher.requiresSwitchUser(request)) {
            // if set, attempt switch and store original
            try {
                Authentication targetUser = attemptSwitchUser(request);

                // update the current context to the new target user
                SecurityContextHolder.getContext().setAuthentication(targetUser);

                // redirect to target url
                this.successHandler.onAuthenticationSuccess(request, response,
                        targetUser);
            }
            catch (AuthenticationException e) {
                this.logger.debug("Switch User failed", e);
                this.failureHandler.onAuthenticationFailure(request, response, e);
            }

            return;
        }
        else if (this.switchUserRequestMatcher.requiresExitUser(request)) {
            // get the original authentication object (if exists)
            Authentication originalUser = attemptExitUser(request);

            // update the current context back to the original user
            SecurityContextHolder.getContext().setAuthentication(originalUser);

            // redirect to target url
            this.successHandler.onAuthenticationSuccess(request, response, originalUser);

            return;
        }

        chain.doFilter(request, response);
    }

    protected Authentication attemptSwitchUser(HttpServletRequest request)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken targetUserRequest;

        String username = request.getParameter(this.usernameParameter);
        String tenantCode = request.getParameter(this.tenantCodeParameter);

        if (username == null) {
            username = "";
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Attempt to switch to user [" + username + "]");
        }

        UserDetails targetUser = this.userDetailsService.loadUserByUsername(username, tenantCode);
        this.userDetailsChecker.check(targetUser);

        // OK, create the switch user token
        targetUserRequest = createSwitchUserToken(request, targetUser);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Switch User Token [" + targetUserRequest + "]");
        }

        // publish event
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new AuthenticationSwitchUserEvent(
                    SecurityContextHolder.getContext().getAuthentication(), targetUser));
        }

        return targetUserRequest;
    }

    private UsernamePasswordAuthenticationToken createSwitchUserToken(
            HttpServletRequest request, UserDetails targetUser) {

        UsernamePasswordAuthenticationToken targetUserRequest;

        // grant an additional authority that contains the original Authentication object
        // which will be used to 'exit' from the current switched user.

        Authentication currentAuth;

        try {
            // SEC-1763. Check first if we are already switched.
            currentAuth = attemptExitUser(request);
        }
        catch (AuthenticationCredentialsNotFoundException e) {
            currentAuth = SecurityContextHolder.getContext().getAuthentication();
        }

        GrantedAuthority switchAuthority = new SwitchUserGrantedAuthority(
                this.switchAuthorityRole, currentAuth);

        // get the original authorities
        Collection<? extends GrantedAuthority> orig = targetUser.getAuthorities();

        // Allow subclasses to change the authorities to be granted
        if (this.switchUserAuthorityChanger != null) {
            orig = this.switchUserAuthorityChanger.modifyGrantedAuthorities(targetUser,
                    currentAuth, orig);
        }

        // add the new switch user authority
        List<GrantedAuthority> newAuths = new ArrayList<>(orig);
        newAuths.add(switchAuthority);

        // create the new authentication token
        targetUserRequest = new UsernamePasswordAuthenticationToken(targetUser,
                targetUser.getPassword(), newAuths);

        // set details
        targetUserRequest
                .setDetails(this.authenticationDetailsSource.buildDetails(request));

        return targetUserRequest;
    }

    protected Authentication attemptExitUser(HttpServletRequest request)
            throws AuthenticationCredentialsNotFoundException {
        // need to check to see if the current user has a SwitchUserGrantedAuthority
        Authentication current = SecurityContextHolder.getContext().getAuthentication();

        if (null == current) {
            throw new AuthenticationCredentialsNotFoundException(
                    this.messages.getMessage("SwitchUserFilter.noCurrentUser",
                            "No current user associated with this request"));
        }

        // check to see if the current user did actual switch to another user
        // if so, get the original source user so we can switch back
        Authentication original = resolver.getSourceAuthentication(current);

        if (original == null) {
            this.logger.debug("Could not find original user Authentication object!");
            throw new AuthenticationCredentialsNotFoundException(
                    this.messages.getMessage("SwitchUserFilter.noOriginalAuthentication",
                            "Could not find original Authentication object"));
        }

        // get the source user details
        UserDetails originalUser = null;
        Object obj = original.getPrincipal();

        if ((obj != null) && obj instanceof UserDetails) {
            originalUser = (UserDetails) obj;
        }

        // publish event
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(
                    new AuthenticationSwitchUserEvent(current, originalUser));
        }

        return original;
    }

    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,
                "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
}
