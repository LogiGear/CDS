package com.logigear.crm.authenticate.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.logigear.crm.authenticate.model.LdapUser;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.service.LdapUserService;
import com.logigear.crm.authenticate.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLdapAuthenticationProvider implements AuthenticationProvider {
	
	private final LdapUserService ldapUserService;
	
	private final UserService userService;
	
	@Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
        String uid = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
        	LdapUser ldapUser = ldapUserService.loadUser(uid, password);
        	if(ldapUser != null) {
        		User user = userService.save(ldapUser);
        		UserDetails userDetails = new UserPrincipal(user);
        		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        	}
        } catch (Exception e) {
        	throw new BadCredentialsException("External system authentication failed");
        }
        
        return null;
    }

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    } 
}