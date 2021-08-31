package com.logigear.crm.admins.service;

import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import com.logigear.crm.admins.repository.RoleRepository;
import com.logigear.crm.admins.repository.UserRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {
	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
	
	public List<Role> getRoleByUserID(long id) {
		return roleRepository.findListRoleByUser(id);
	}


	public void validRoles(RoleName role, String email) {
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));	
		if(!user.getRoles().contains(roleRepository.findByName(role))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"user does not have the valid role");
		}
	}
}
