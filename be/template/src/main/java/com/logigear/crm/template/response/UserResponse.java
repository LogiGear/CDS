package com.logigear.crm.template.response;

import com.logigear.crm.template.model.Role;
import com.logigear.crm.template.model.RoleName;
import com.logigear.crm.template.model.User;
import com.logigear.crm.template.model.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponse {

	private Long id;
	private String email;
	private String name;
	private Set<Role> roles = new HashSet<>();
	private UserStatus userStatus;

	public UserResponse(User user) {
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
		roles = user.getRoles();
		userStatus = user.getStatus();
		List<RoleName> rolesName = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
	}

}
