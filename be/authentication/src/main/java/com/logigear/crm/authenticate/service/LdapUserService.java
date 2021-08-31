package com.logigear.crm.authenticate.service;

import com.logigear.crm.authenticate.model.LdapUser;


public interface LdapUserService {

	LdapUser loadUser(String userName, String password) throws Exception;
}
