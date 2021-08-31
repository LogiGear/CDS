package com.logigear.crm.admins.service.impl;

import com.logigear.crm.admins.exception.ResourceNotFoundException;
import com.logigear.crm.admins.model.Role;
import com.logigear.crm.admins.model.RoleName;
import com.logigear.crm.admins.model.User;
import com.logigear.crm.admins.payload.UserUpdateRequest;
import com.logigear.crm.admins.repository.RoleRepository;
import com.logigear.crm.admins.repository.UserRepository;
import com.logigear.crm.admins.service.EmployeeService;
import com.logigear.crm.admins.service.UserService;
import com.logigear.crm.admins.util.MessageUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

	private final static String USER_NOT_FOUND_MSG = "User with email %s not found";
	private final static String TOKEN_NOT_FOUND = "Token already expired or does not exist";
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository,
						   RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public void save(User user){
		userRepository.save(user);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User updateUserRoles(UserUpdateRequest req) {
		User user = userRepository.findById(req.getId()).orElseThrow(NoSuchElementException::new);
		Set<Role> roles = new HashSet<>();
		for(long id : req.getRoles()){
			Role role = roleRepository.findById(id).orElseThrow(NoSuchElementException::new);
			roles.add(role);
		}
	
		user.setRoles(roles);
		return userRepository.save(user);
	}


	@Override
	 public User findUserById(Long id){
		return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
	 }

	 @Override
	 public List<User> findAll(){
		return userRepository.findAll();
	 }

	@Override
	public List<Role> getUserByRole(RoleName roleName) {
		Role role = roleRepository.findByName(roleName);
		List<Role> roles= roleRepository.findByRole(role.getId());
		return roles;
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(
				() -> new ResourceNotFoundException(
						MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""))
		);
	}

	

}
