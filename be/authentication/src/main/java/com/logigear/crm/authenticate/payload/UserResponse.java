package com.logigear.crm.authenticate.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.logigear.crm.authenticate.model.Role;
import com.logigear.crm.authenticate.model.RoleName;
import com.logigear.crm.authenticate.model.User;
import com.logigear.crm.authenticate.model.UserStatus;

@Getter @Setter @ToString
public class UserResponse {

	private Long id;
	private String email;
	private String name;
	private Set<Role> roles = new HashSet<>();
	private UserStatus userStatus;
	
	private boolean unverified = false;
	private boolean blocked = false;
	private boolean admin = false;
	private boolean goodUser = false;
	private boolean goodAdmin = false;
	private String token = "";
	
	public UserResponse(User user) {
		
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
		roles = user.getRoles();
		userStatus = user.getStatus();
		List<RoleName> rolesName = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
		admin = rolesName.contains(RoleName.ADMIN);
		goodUser = !(unverified || blocked);
		goodAdmin = goodUser && admin;
		token = token;
	}
}
