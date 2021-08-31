package com.logigear.crm.admins.model.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

public class Role_User_Key implements Serializable{
	
	@Column(name = "user_id")
	long userId;
	
	@Column(name = "role_id")
	long roleId;
	
}
