package com.logigear.crm.admins.response;

import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @ToString
public class UserResponse {

	private Long id;
	private String email;
	private String name;
	private Set<Role> roles = new HashSet<>();

	public UserResponse(User user) {
		
		id = user.getId();
		name = user.getName();
		email = user.getEmail();
		roles = user.getRoles();
		List<RoleName> rolesName = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
	}

	//   public void setToken(String token) {
    //     this.token = token;
    // }
}
