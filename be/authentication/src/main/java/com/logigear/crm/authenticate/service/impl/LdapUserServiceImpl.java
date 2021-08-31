package com.logigear.crm.authenticate.service.impl;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Objects;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.logigear.crm.authenticate.model.LdapUser;
import com.logigear.crm.authenticate.service.LdapUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LdapUserServiceImpl implements LdapUserService {

	@Value("${ldap.urls}")
	private String url;
	
	@Value("${ldap.base-dn}")
	private String baseDn;
	
	private String DOMAIN_NAME = "logigear.com";
	private static String ACCOUNT_NOT_DISABLE = "(!(userAccountControl:1.2.840.113556.1.4.803:=2))";
	private String ACCOUNT_NOT_EXPIRED = "";
	
	public LdapUser loadUser(String userName, String password) throws  Exception {
		Hashtable<String, String> props = new Hashtable<String, String>();
		String principalName = userName;
		
		if(!userName.contains("@")){
			principalName = userName + "@" + DOMAIN_NAME;
		} else {
			principalName = userName;
		}
		props.put(Context.SECURITY_PRINCIPAL, principalName);
		if (password != null) {
			props.put(Context.SECURITY_CREDENTIALS, password);
		}
		
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL, url);
		props.put(Context.SECURITY_AUTHENTICATION, "simple");
		props.put(Context.REFERRAL, "ignore");
		DirContext context = null;
		LdapUser result = null;
		try {
			context = new InitialLdapContext(props, null); // authenticate
			
			String[] userAttributes = {"name", "displayName", "mail", "physicalDeliveryOfficeName", "title", "sAMAccountName"};
			
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SUBTREE_SCOPE);
			controls.setReturningAttributes(userAttributes);
			GregorianCalendar epochTime = new GregorianCalendar(1601,Calendar.JANUARY,1);
			Date epochDate = epochTime.getTime();
		    // Note that 1/1/1601 will be returned as a negative value by Java
			GregorianCalendar today = new GregorianCalendar();
			Date todaysDate = today.getTime();
			long timeSinceEpoch = 10000 * (todaysDate.getTime() - epochDate.getTime());
			ACCOUNT_NOT_EXPIRED = "(|(!(accountExpires=*))(accountExpires=9223372036854775807)(accountExpires=0)(accountExpires>=" +timeSinceEpoch +"))";
		        
			String filter = String.format("(& (userPrincipalName=%s)(objectClass=user)%s%s)", principalName, ACCOUNT_NOT_DISABLE, ACCOUNT_NOT_EXPIRED);
			NamingEnumeration<SearchResult> answer = context.search(baseDn, filter, controls);
			if (answer.hasMore()) {
				Attributes attrs = answer.next().getAttributes();
				Attribute user = attrs.get("mail");
				if (user != null) {	
					result = new LdapUser(
							Objects.toString(attrs.get("name").get(), ""),
							Objects.toString(attrs.get("displayName").get(), ""),
							Objects.toString(attrs.get("mail").get(), ""),
							Objects.toString(attrs.get("physicalDeliveryOfficeName").get(), ""),
							Objects.toString(attrs.get("title").get(), ""),
							Objects.toString(attrs.get("sAMAccountName").get(), ""),
							password);
		        } 
			}
		} finally {
			if (context != null) {
				context.close();
			}
		}
		
		return result;
	}
}
