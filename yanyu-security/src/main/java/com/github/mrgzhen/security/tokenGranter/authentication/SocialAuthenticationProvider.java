package com.github.mrgzhen.security.tokenGranter.authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class SocialAuthenticationProvider implements AuthenticationProvider {

	private SocialUserDetailService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken)authentication;
		UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal(), authenticationToken.getApp());
		if(user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}
		SocialAuthenticationToken authenticationResult = new SocialAuthenticationToken(user,authenticationToken.getApp(), user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SocialAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public SocialUserDetailService getSocialUserDetailService() {
		return userDetailsService;
	}

	public void setSocialUserDetailService(SocialUserDetailService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
