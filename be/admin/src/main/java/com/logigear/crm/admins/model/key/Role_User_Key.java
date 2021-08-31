package com.logigear.crm.admins.model.key;

import java.io.Serializable;

import javax.persistence.Column;

public class Role_User_Key implements Serializable{
	
	@Column(name = "user_id")
	long userId;
	
	@Column(name = "role_id")
	long roleId;
	
}
