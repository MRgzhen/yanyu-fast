package com.github.mrgzhen.security.tokenGranter.authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

	private SmsUserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken)authentication;
		UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
		if(user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}
		SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public SmsUserDetailsService getSmsUserDetailsService() {
		return userDetailsService;
	}

	public void setSmsUserDetailsService(SmsUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
