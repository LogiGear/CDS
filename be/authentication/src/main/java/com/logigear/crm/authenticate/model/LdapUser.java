package com.logigear.crm.authenticate.model;

import lombok.Value;

@Value
public class LdapUser {
	private final String name;
	private final String displayName;
	private final String email;
	private final String physicalDeliveryOfficeName;
	private final String title;
	private final String username;
	private final String password;
}
